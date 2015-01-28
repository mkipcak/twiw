package com.twiw.trackman;

import java.util.ArrayList;
import java.util.List;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;

public class TrackOptimizer {
	
	List<Talk> allTalks;
	

	public Conference getResultContainers(){
		//XXX-TODO do-nothing for now
		return null;
	}
	public void pack(Talk tk){
		List<Talk> talks = new ArrayList<Talk>();
		this.pack(talks);
	}
	
	public void pack(List<Talk> talks){
		
	}
}
