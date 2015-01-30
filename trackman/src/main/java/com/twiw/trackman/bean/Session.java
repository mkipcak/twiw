package com.twiw.trackman.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Session implements Iterable<Talk>{
	private int volume;
	private int usedSpace;
	private List<Talk> talks = new ArrayList<Talk>();
	
	public Session(int volumeInMin) {
		super();
		this.volume = volumeInMin;
	}

	public int getVolume() {
		return volume;
	}
	public boolean hasEnoughSpace(Talk talk) {
		return this.volume >= (this.usedSpace + talk.getValue());
	}
	public boolean add(Talk talk) {
		if(hasEnoughSpace(talk)) {
			this.talks.add(talk);
			this.usedSpace += talk.getValue();
			return true;
		}
		return false;
	}
	public int getRemainingSpace(){
		return this.volume - this.usedSpace;
	}
	
	public String toString(){
		return "session(#"+Integer.toHexString(this.hashCode()).toUpperCase() + ",vol="+this.getVolume()+ ",rem="+this.getRemainingSpace()+")";
	}

	public Iterator<Talk> iterator() {
		return talks.iterator();
	}

	public int size() {
		return talks.size();
	}
}
