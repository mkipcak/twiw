package com.twiw.trackman;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twiw.trackman.bean.Talk;

public class TalkBuilder {
	static final String TALKTITLE_LUNCH = "Lunch";
	static final String TALKTITLE_NETWORK = "Networking Event";
	static final String REGULARLINE_MINUTESUFFIX = "min";
	static final String LIGHTKEYWORD = "lightning";
	static final int LIGHTVAL_INMIN = 5;

	public Talk build(String line){
		
		if(line == null||line.trim().length() == 0) {
			return null;
		} 
		else if(line.trim().endsWith(LIGHTKEYWORD)){
			return createForLightinging(line);
		}
		else if(line.endsWith(REGULARLINE_MINUTESUFFIX)){
			return createForRegularLine(line);
		}
		return null;
	}
	private Talk createForRegularLine(String line) {
        Pattern linePattern = Pattern.compile("(.*?)(\\d+)(.*)");
        Matcher m = linePattern.matcher(line);
        if (!m.matches()) {
                        return null;
        }
        String g1 = m.group(1);
        String g2 = m.group(2);
        String g3 = m.group(3);
        //check g1
        if(g1.trim().length() == 0){
                        return null;
        }
        //check g2
        int vol = -1;
        try {
                        vol = Integer.parseInt(g2);
        } catch (NumberFormatException e) {
                        return null;
        }
        //check g3
        if(!g3.equals(REGULARLINE_MINUTESUFFIX)){
                        return null;
        }
        return new Talk(g1.trim(), vol, false);                                  
}
	private Talk createForLightinging(String line){
		Pattern linePattern = Pattern.compile("(.*?)"+LIGHTKEYWORD);
		Matcher m = linePattern.matcher(line);
		if (m.matches()) {
		  return new Talk(m.group(1).trim(),LIGHTVAL_INMIN, true);
		}
		return new Talk("unknown",LIGHTVAL_INMIN, true);
	}
	public List<Talk> buildAll(int... minutes){
		List<Talk> l = new ArrayList<Talk>();
		for (int min : minutes) {
			Talk talk = build("test "+min+REGULARLINE_MINUTESUFFIX);
			if(talk != null) {
				l.add(talk);
			}
		}
		return l;
	}
	public List<Talk> buildAll(String content) {
        List<Talk> l = new ArrayList<Talk>();
        content = content.replaceAll("\\r\\n", "\n");
        String[] lines = content.trim().split("\n");
		for (String line : lines) {
			Talk talk = build(line);
			if(talk != null) {
				l.add(talk);
			}
		}
		return l;
	}
	public Talk buildNoVolume(String title) {
		return new Talk(title, 0, false);
	}
}
