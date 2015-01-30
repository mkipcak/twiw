package com.twiw.trackman;

import com.twiw.trackman.bean.Session;

public class SessionFactory {
	
	int[] volumes 	= null;
	int currentIdx 	= -1;
	int maxVol = -1;
    
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
		Session sess = new Session(vol);
		return sess;
	};
	public int findLargestVolumeInaSession() {
        if(this.maxVol == -1) {
                        int max = Integer.MIN_VALUE;
                        for(int i = 0; i < volumes.length; i++) {
                              if(volumes[i] > max) {
                                 max = volumes[i];
                              }
                        }
                        this.maxVol= max;
        }
        return this.maxVol;
};
}
