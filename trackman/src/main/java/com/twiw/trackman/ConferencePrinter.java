package com.twiw.trackman;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;
import com.twiw.trackman.bean.Track;

public class ConferencePrinter {
	
	private static final String HH_MMAA = "hh:mmaa";
	private static final String[] SESSION_START_TIMES = {"09:00AM","12:00PM","01:00PM"}; 
	
	public void print(Conference conf, Writer w){
		
		TalkBuilder tb = new TalkBuilder();
		Session sessLunch = new Session(60);
		sessLunch.add(tb.buildNoVolume(TalkBuilder.TALKTITLE_LUNCH));
		
		PrintWriter pw = new PrintWriter(w); 
		for (Track trck : conf) {
			pw.println("Track " + trck.getId()+":");			
			this.print(pw, trck.getFirst(), SESSION_START_TIMES[0]);			
			this.print(pw, sessLunch,SESSION_START_TIMES[1]);			
			this.print(pw, trck.getLast(), SESSION_START_TIMES[2]);			
			pw.println();
		}
		pw.flush();
	}
	private void print(PrintWriter pw, Session sess, String beginAt) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(HH_MMAA);
			Date dtBegin 		= df.parse(beginAt);
			Calendar cldr	 	= GregorianCalendar.getInstance();
			cldr.setTime(dtBegin);
			for (Talk t : sess) {
				if(t.isLightining()){
					pw.println(df.format(cldr.getTime()) + " " +t.getTitle() + " " + TalkBuilder.LIGHTKEYWORD);
					
				} else if(t.getValue() > 0){ 
					pw.println(df.format(cldr.getTime()) + " " +t.getTitle() + " " + t.getValue() + TalkBuilder.REGULARLINE_MINUTESUFFIX);
					
				} else if(t.getValue() == 0){ 
					pw.println(df.format(cldr.getTime()) + " " +t.getTitle());
				}
				cldr.add(Calendar.MINUTE, t.getValue());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
