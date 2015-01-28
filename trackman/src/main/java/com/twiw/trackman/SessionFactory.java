package com.twiw.trackman;

import com.twiw.trackman.bean.Session;

public class SessionFactory {
	
	int[] volumes 	= null;
	int currentIdx 	= -1;
	
	public SessionFactory(int[] volumes) {
		super();
		this.volumes = volumes;
	}
	private int findNextVol(){
		this.currentIdx = currentIdx == (volumes.length-1) 
							? 0 : currentIdx + 1;
		return volumes[currentIdx];
	}
	public synchronized Session create(){
		if(volumes == null) {
			return null;
		}
		int vol = findNextVol();
		return new Session(vol);
	};
}
