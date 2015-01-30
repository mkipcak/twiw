package com.twiw.trackman;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Track;

class Context{
	//given rules & states
	String[] sessionStartTimes = {"09:00AM","01:00PM"}; 
	String networkEventHighValue = "05:00PM";
	String networkEventLowValue = "04:00PM";
	String lunchTime = "12:00PM";
	String timeFormat = "hh:mmaa";
	int maxSessionPerTrack = 2;
	
	int trackCounter = 1;
	
	//runtime states
	private Conference conf;
	private Track track;
	private Session session;
	private SessionFactory sessionFactory;
	private TalkBuilder talkBuilder;
	
	public Context(TalkBuilder talkBuilder, SessionFactory sf, Conference conf) {
		super();
		this.setTalkBuilder(talkBuilder);
		this.setSessionFactory(sf);
		this.setConference(conf);
	}
	public Conference getConference() {
		return conf;
	}
	public void setConference(Conference conf) {
		this.conf = conf;
	}
	TalkBuilder getTalkBuilder() {
		return talkBuilder;
	}
	void setTalkBuilder(TalkBuilder talkBuilder) {
		this.talkBuilder = talkBuilder;
	}
	Session getSession() {
		return session;
	}
	Session setSession(Session sess) {
		this.session = sess;
		return sess;
	}
	Track getTrack() {
		return track;
	}
	void setTrack(Track track) {
		this.track = track;
	}
	SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	

}