package com.twiw.trackman.bean;

import java.util.ArrayList;
import java.util.List;

public class Session {
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
		return this.usedSpace == 0 
				|| this.volume >= (this.usedSpace + talk.getValue());
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
}
