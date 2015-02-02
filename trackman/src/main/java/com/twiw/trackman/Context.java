package com.twiw.trackman;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Track;

class Context{
	//given rules & states
	private String[] sessionStartTimes = {"09:00AM","01:00PM"}; 
	private String networkEventHighValue = "05:00PM";
	private String networkEventLowValue = "04:00PM";
	private String lunchTime = "12:00PM";
	private String timeFormat = "hh:mmaa";
	private int maxSessionPerTrack = 2;
	
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
		return getConf();
	}
	public void setConference(Conference conf) {
		this.setConf(conf);
	}
	public TalkBuilder getTalkBuilder() {
		return talkBuilder;
	}
	public 	void setTalkBuilder(TalkBuilder talkBuilder) {
		this.talkBuilder = talkBuilder;
	}
	public Session getSession() {
		return session;
	}
	public Session setSession(Session sess) {
		this.session = sess;
		return sess;
	}
	public Track getTrack() {
		return track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	public String[] getSessionStartTimes() {
		return sessionStartTimes;
	}
	public void setSessionStartTimes(String[] sessionStartTimes) {
		this.sessionStartTimes = sessionStartTimes;
	}
	public String getNetworkEventHighValue() {
		return networkEventHighValue;
	}
	public void setNetworkEventHighValue(String networkEventHighValue) {
		this.networkEventHighValue = networkEventHighValue;
	}
	public String getNetworkEventLowValue() {
		return networkEventLowValue;
	}
	public void setNetworkEventLowValue(String networkEventLowValue) {
		this.networkEventLowValue = networkEventLowValue;
	}
	public String getLunchTime() {
		return lunchTime;
	}
	public void setLunchTime(String lunchTime) {
		this.lunchTime = lunchTime;
	}
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	public Conference getConf() {
		return conf;
	}
	public void setConf(Conference conf) {
		this.conf = conf;
	}
	public int getMaxSessionPerTrack() {
		return maxSessionPerTrack;
	}
	public void setMaxSessionPerTrack(int maxSessionPerTrack) {
		this.maxSessionPerTrack = maxSessionPerTrack;
	}
	

}