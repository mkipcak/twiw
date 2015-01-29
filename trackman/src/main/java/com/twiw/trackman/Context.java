package com.twiw.trackman;

import java.util.ArrayList;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Track;

class Context{
	static final int MAXSESSIONCOUNT_PERTRACK = 2;
	
	Conference conf;
	Track day;
	Session sess;
	SessionFactory sf;
	
	public void createSessionAndDayIfNeeded(){
		this.sess = this.sf.create();
		if(this.day.getSessionCount() == MAXSESSIONCOUNT_PERTRACK){
			this.day = new Track(new ArrayList<Session>());
			this.conf.add(this.day);
		}
		this.day.add(this.sess);
	}
}