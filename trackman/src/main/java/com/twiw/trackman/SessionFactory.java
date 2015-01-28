package com.twiw.trackman;

import com.twiw.trackman.bean.Session;

public class SessionFactory {
	
	int[] volumes 	= new int[]{3*60,4*60};
	int currentIdx 	= -1;
	
	private int findNextVol(){
		this.currentIdx = currentIdx == (volumes.length-1) 
							? 0 : currentIdx + 1;
		return volumes[currentIdx];
	}
	public synchronized Session create(){
		int vol = findNextVol();
		return new Session(vol);
	};
}
