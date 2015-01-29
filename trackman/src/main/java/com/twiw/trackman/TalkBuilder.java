package com.twiw.trackman;

import java.util.ArrayList;
import java.util.List;

import com.twiw.trackman.bean.Talk;

public class TalkBuilder {
	
	static final String LIGHTKEYWORD = "lightning";
	static final int LIGHTVAL_INMIN = 5;

	public Talk build(String line){
		if(line == null) {
			return null;
		} 
		else if(line.endsWith(LIGHTKEYWORD)){
			return new Talk(LIGHTVAL_INMIN);
		}
		else if(line.endsWith("min")){
			String[] parts = line.split(" ");
			String last = parts[parts.length-1];
			String minAsStr = last.substring(0, last.length()-3);
			return new Talk(Integer.parseInt(minAsStr));
		}
		else {
			return null;
		}
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
		String[] lines = content.split("\n");
		for (String line : lines) {
			Talk talk = build(line);
			if(talk != null) {
				l.add(talk);
			}
		}
		return l;
	}
}
