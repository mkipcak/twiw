package com.twiw.trackman.bean;

import java.util.Iterator;
import java.util.List;

public class Track {

	private List<Session> sessions;
	
	public Track(List<Session> sessions) {
		super();
		this.sessions = sessions;
	}

	public Iterator<Session> iterator() {
		return sessions.iterator();
	}
 
	public boolean isEmpty() {
		return sessions.isEmpty();
	}

	public boolean add(Session e) {
		return sessions.add(e);
	}

	public int getSessionCount() {
		return sessions.size();
	}

	public Session getFirst(){
		return sessions.get(0);
	}
	public Session getLast(){
		return sessions.get(sessions.size()-1);
	}
	
}
