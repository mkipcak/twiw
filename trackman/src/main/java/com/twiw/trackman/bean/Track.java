package com.twiw.trackman.bean;

import java.util.List;

public class Track {
	private List<Session> sessions;
	
	public Track(List<Session> sessions) {
		super();
		this.sessions = sessions;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public boolean isEmpty() {
		return sessions.isEmpty();
	}

	public boolean add(Session e) {
		return sessions.add(e);
	}


}
