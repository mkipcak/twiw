package com.twiw.trackman;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Track;

class Context{
	int trackCounter = 1;
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