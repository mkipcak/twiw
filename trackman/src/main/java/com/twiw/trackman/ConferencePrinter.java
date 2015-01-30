package com.twiw.trackman;

import java.io.PrintWriter;
import java.io.Writer;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;
import com.twiw.trackman.bean.Track;

public class ConferencePrinter {
	
	public void print(Conference conf, Writer w){
		
		PrintWriter pw = new PrintWriter(w); 
		for (Track trck : conf) {
			pw.println("Track " + trck.getId()+":");
			for (Session sess : trck) {
				this.print(pw, sess);	
			}
			pw.println();
		}
		pw.flush();
	}
	private void print(PrintWriter pw, Session sess) {
		
			for (Talk t : sess) {
				if (t.getTitle() == null || t.getTitle().trim().length() == 0){
					
				}
				else if(t.isLightining()){
					pw.println(t.getStartTime() + " " +t.getTitle() + " " + TalkBuilder.LIGHTKEYWORD);
					
				} else if(t.getValue() > 0){ 
					pw.println(t.getStartTime() + " " +t.getTitle() + " " + t.getValue() + TalkBuilder.REGULARLINE_MINUTESUFFIX);
					
				} else if(t.getValue() == 0){ 
					pw.println(t.getStartTime() + " " +t.getTitle());
				}
			}
	}
}
