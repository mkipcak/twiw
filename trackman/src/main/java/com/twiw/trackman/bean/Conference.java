package com.twiw.trackman.bean;

import java.util.List;

public class Conference {
	private List<Session> sessions;

	public Conference(List<Session> sessions) {
		super();
		this.sessions = sessions;
	}

	public int size() {
		return sessions.size();
	}

	public boolean isEmpty() {
		return sessions.isEmpty();
	}

}
