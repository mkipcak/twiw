package com.twiw.trackman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Talk;

/**
 * Hello TW!
 *
 */
public class App {

	private static final String JOINTCHAR = ",";

	private static String readFile(String fname) throws FileNotFoundException,
			IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String content = sb.toString();
			return content;
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		PrintWriter pw = new PrintWriter(System.out);
		pw.println("PARAMS: " + Arrays.toString(args));

		String format = "human";
		List<String> talkLists = new ArrayList<String>();
		// -----------
		if (args.length == 0) {
			System.out
					.println("Usage:   trackman <options> \"[params]\"\r\n\r\n"
							+ "Available options:\r\n"
							+ " -s \"[GIVENLIST]\"                  processes list which is given in param([GIVENLIST]) value, like -> \"...$trackman -s \"first 1min[CRLF]second 20min\"\r\n"
							+ " -f \"[ABSFILEPATH]\"                            processes list which is stored in given file([ABSFILEPATH]), like -> \"...$ trackman -f \"\\tmp\\longestList.txt\"\r\n"
							+ " -b1 \"[LOW],[HIGH]\"                           generates a single benchmark talk list which has random talks.Talk count is between[LOW],[HIGH] values for 60 minutes, like \"...$trackman -r \"5-10\"\r\n"
							+ " -b2 \"[P, Q, R]\"                       generates random talk list(count=[P]), with talks(count=[Q]), with random minutes(maxValue=[R]) for benchmarking,like \"...$trackman -b2 \"5,17,60\"\r\n"
							+ "\r\n"
							+ " -format \"human|csv|both\"           additional parameter for formatting standart output in humanReadable, in csv or both format");
			return;

		} else if (args.length > 1 && args[0].equals("-s")
				&& args[1].trim().length() > 0) {

			talkLists.add(args[1].trim());

		} else if (args.length > 1 && args[0].equals("-f")
				&& args[1].trim().length() > 0) {

			File f = new File(args[1].trim());
			pw.println("DESC  : file \"" + f.getName()
					+ "\" will be processed.");
			talkLists.add(readFile(args[1].trim()));

		} else if (args.length > 1 && args[0].equals("-b1")
				&& args[1].trim().length() > 0) {

			talkLists.add(benchmark1(args[1], pw));

		} else if (args.length > 1 && args[0].equals("-b2")
				&& args[1].trim().length() > 0) {

			talkLists.addAll(benchmark2(args[1], pw));
		}
		// -----------
		if (args.length > 2 && args[2].startsWith("-format")
				&& args[3].trim().length() > 0) {

			format = args[3].trim();

		}
		// -----------

		BufferedWriter w = new BufferedWriter(pw, 512);
		pack(talkLists, format, new int[] { 3 * 60, 4 * 60 }, new String[] {
				"09:00AM", "01:00PM" }, w);
		// -----------

	}

	static String STANDART_LINE_TEXT = "SAMPLETALK ";

	private static String benchmark1(String args, PrintWriter pw) {

		Random random = new Random();
		String[] lowhigh = args.split(JOINTCHAR);
		int min = Integer.parseInt(lowhigh[0]);
		int max = Integer.parseInt(lowhigh[1]);

		int talkCount = random.nextInt(max - min + 1) + min;

		pw.println("DESC  : generated random talk count(" + talkCount
				+ ") for range min=" + min + " max=" + max
				+ ", each talk duration is fixed 60 minutes.");

		StringBuilder sb = new StringBuilder(talkCount
				* STANDART_LINE_TEXT.length());
		for (int i = 0; i < talkCount; i++) {
			// sb.append("#" + (i+1) + ".");
			sb.append(STANDART_LINE_TEXT);
			sb.append("60min\r\n");
		}
		return sb.toString();
	}

	private static List<String> benchmark2(String args, PrintWriter pw) {
		Random random = new Random();
		String[] pqr = args.split(JOINTCHAR);
		int maxListCount = Integer.parseInt(pqr[0]);
		int talkCount = Integer.parseInt(pqr[1]);
		int maxMinutes = Integer.parseInt(pqr[2]);
		List<String> talkLists = new ArrayList<String>();
		int listCount = random.nextInt(maxListCount) + 1;

		pw.println("DESC  : generated random "
				+ talkCount
				+ " talk for "
				+ listCount
				+ " conference, with each talk duration is random minutes (maxMinute="
				+ maxMinutes + ")");

		for (int i = 0; i < listCount; i++) {
			StringBuilder sb = new StringBuilder(talkCount
					* STANDART_LINE_TEXT.length());
			for (int j = 0; j < talkCount; j++) {
				// sb.append("#" + (j+1) + ".");
				sb.append(STANDART_LINE_TEXT);
				sb.append(random.nextInt(maxMinutes) + 1);
				sb.append("min\r\n");
			}
			talkLists.add(sb.toString());
		}
		return talkLists;
	}

	private static void pack(List<String> talkLists, String form,
			int[] volumesInMin, String[] sessStartTimes, Writer writer) {

		int idx = 0;
		for (String content : talkLists) {
			try {
				writer.write("\r\n");
				writer.write("" + ++idx
						+ ".conference's output is shown below.");
				writer.write("\r\n");
				writer.write("\r\n");
				TalkBuilder builder = new TalkBuilder();
				List<Talk> talks = builder.buildAll(content);
				TrackOptimizer to = new TrackOptimizer();

				long t0 = System.currentTimeMillis();
				to.pack(talks, volumesInMin, builder, sessStartTimes);
				long t1 = System.currentTimeMillis();

				Conference cnfe = to.getResultContainers();
				ConferencePrinter printer = new ConferencePrinter();

				long t2 = System.currentTimeMillis();
				printer.print(cnfe, writer);
				long t3 = System.currentTimeMillis();

				if (form.equals("human") || form.equals("both")) {
					printoutBenchmarkInHumanReadable(volumesInMin,
							sessStartTimes, talks, cnfe, t0, t1, t2, t3, writer);
					writer.write("\n");
				}
				if (form.equals("csv") || form.equals("both")) {
					writer.write("\n");
					printoutBenchmarkInCsvFormat(volumesInMin, sessStartTimes,
							talks, cnfe, t0, t1, t2, t3, writer);
				}
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void printoutBenchmarkInCsvFormat(int[] volumesInMin,
			String[] sessStartTimes, List<Talk> talks, Conference cnfe,
			long t0, long t1, long t2, long t3, Writer w) throws IOException {

		w.write("sessionVolumes=" + Arrays.toString(volumesInMin));
		w.write(",sessionStartTimes=" + Arrays.toString(sessStartTimes));
		w.write(",talkCount=" + talks.size());
		w.write(",trackCount=" + cnfe.size());
		w.write(",startedAt="
				+ new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(t0)));
		w.write(",packCompleted="
				+ new SimpleDateFormat("ss:SSS").format(new Date(t1 - t0)));
		w.write(",printStarted="
				+ new SimpleDateFormat("HH:mmss:SSS").format(new Date(t2)));
		w.write(",printCompleted="
				+ new SimpleDateFormat("ss:SSS").format(new Date(t3 - t2)));
		w.write(",overallDuration="
				+ new SimpleDateFormat("ss:SSS").format(new Date(t3 - t0)));
	}

	private static void printoutBenchmarkInHumanReadable(int[] volumesInMin,
			String[] sessStartTimes, List<Talk> talks, Conference cnfe,
			long t0, long t1, long t2, long t3, Writer w) throws IOException {
		String sfx = " ms.";
		int len = 50;
		w.write(padRight(
				"__________________________________________________\n", len));
		w.write(padRight(
				"|                                                 |\n", len));
		w.write(padRight(
				"|___________________BENCHMARKS____________________|\n", len));
		w.write(padRight(
				"|_________________________________________________|\n", len));
		w.write(padRight(
				"| session volumes   : " + Arrays.toString(volumesInMin), len)
				+ "|\n");
		w.write(padRight(
				"| session startTimes: " + Arrays.toString(sessStartTimes), len)
				+ "|\n");
		w.write(padRight("| talk count        : " + talks.size(), len) + "|\n");
		w.write(padRight("| track count       : " + cnfe.size(), len) + "|\n");
		w.write(padRight("| pack started at   : "
				+ new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(t0)),
				len)
				+ "|\n");
		w.write(padRight("| print started at  : "
				+ new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(t2)),
				len)
				+ "|\n");
		w.write(padRight("| pack duration     : "
				+ new SimpleDateFormat("ss,SSS").format(new Date(t1 - t0))
				+ sfx, len)
				+ "|\n");
		w.write(padRight("| print duration    : "
				+ new SimpleDateFormat("ss,SSS").format(new Date(t3 - t2))
				+ sfx, len)
				+ "|\n");
		w.write(padRight("| overall duration  : "
				+ new SimpleDateFormat("ss,SSS").format(new Date(t3 - t0))
				+ sfx, len)
				+ "|\n");
		w.write(padRight("|_________________________________________________|",
				len));
	}

	private static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
}