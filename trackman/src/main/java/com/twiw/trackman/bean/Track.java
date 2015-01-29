package com.twiw.trackman.bean;

import java.util.Iterator;
import java.util.List;

public class Track implements Iterable<Session>{
	public int id;
	private List<Session> sessions;
	
	public Track(int id, List<Session> sessions) {
		super();
		this.id = id;
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
	public int calculateVolume(){
		int total = 0;
		if(sessions != null) {
			for (Session sess : sessions) {
				total += sess.getVolume();
			}
		}
		return total;
	}
	public int calculateRemainingSpace(){
		int total = 0;
		if(sessions != null) {
			for (Session sess : sessions) {
				total += sess.getRemainingSpace();
			}
		}
		return total;
	}
	public String toString() {
		
		return "day(#" + Integer.toHexString(this.hashCode()).toUpperCase() 
				+ ",["+(this.sessions==null?null:this.sessions.size())+"]"
				+ ",rem="+calculateRemainingSpace()
				+ ",vol="+calculateVolume()
				+")";
	}

	public int getId() {
		return id;
	}
}
