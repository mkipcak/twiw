package com.twiw.trackman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Talk;

/**
 * Hello TW!
 *
 */
public class App {
	
	private static String readFile(String fname) throws FileNotFoundException, IOException {
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

	public static void main(String[] args) throws FileNotFoundException, IOException {

		if (args.length == 0) {
			System.out.println("usage: trackman -s \"[yourTalkList]\" [ENTER], or trackman -f \"[filepath]\" [ENTER]");

		} else if (args.length > 1 && args[0].equals("-s")
				&& args[1].trim().length() > 0) {

			pack(args[1].trim());

		} else if (args.length > 1 && args[0].equals("-f")
				&& args[1].trim().length() > 0) {
			
			pack(readFile(args[1].trim()));
		}
	}
	private static void pack(String content) {
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin = new int[] { 3 * 60, 4 * 60 };
		List<Talk> talks = builder.buildAll(content);
		TrackOptimizer to = new TrackOptimizer();
		to.pack(talks, volumesInMin, builder, new String[] { "09:00AM","01:00PM" });
		Conference cnfe = to.getResultContainers();

		ConferencePrinter printer = new ConferencePrinter();
		printer.print(cnfe, new PrintWriter(System.out));
	}
}
