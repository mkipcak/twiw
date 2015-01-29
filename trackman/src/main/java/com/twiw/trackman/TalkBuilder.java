package com.twiw.trackman;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twiw.trackman.bean.Talk;

public class TalkBuilder {
	
	static final String LIGHTKEYWORD = "lightning";
	static final int LIGHTVAL_INMIN = 5;

	public Talk build(String line){
		
		if(line == null) {
			return null;
		} 
		else if(line.trim().endsWith(LIGHTKEYWORD)){
			return new Talk(LIGHTVAL_INMIN);
		}
		else if(line.endsWith("min")){
			Pattern linePattern = Pattern.compile("(.*?)(\\d+)(.*)");
			Matcher m = linePattern.matcher(line);
			if (m.matches()) {
			  String title = m.group(1);
			  String minAsStr = m.group(2);
			  int volInMinutes = Integer.parseInt(minAsStr);
			  return new Talk(title, volInMinutes);
			}
		}
		return null;
	}
	public List<Talk> buildAll(int... minutes){
		List<Talk> l = new ArrayList<Talk>();
		for (int min : minutes) {
			Talk talk = build("t "+min+"min");
			if(talk != null) {
				l.add(talk);
			}
		}
		return l;
	}
	public List<Talk> buildAll(String content) {
		List<Talk> l = new ArrayList<Talk>();
		String[] lines = content.trim().split("\n");
		for (String line : lines) {
			Talk talk = build(line);
			if(talk != null) {
				l.add(talk);
			}
		}
		return l;
	}
}
