package com.twiw.trackman;

import java.util.ArrayList;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Track;

class Context{
	static final int MAXSESSIONCOUNT_PERTRACK = 2;
	private int trackCounter = 1;
	Conference conf;
	Track day;
	Session sess;
	SessionFactory sf;
	
	public void addSessionIfNeededAddDay(){
		this.sess = this.sf.create();
		if(this.day.getSessionCount() == MAXSESSIONCOUNT_PERTRACK){
			this.addTrack();
		}
		this.day.add(this.sess);
	}
	
	public void addTrack(){
		this.day = new Track(trackCounter++, new ArrayList<Session>());
		this.conf.add(this.day);
	}
}