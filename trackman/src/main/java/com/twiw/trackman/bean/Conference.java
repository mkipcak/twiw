package com.twiw.trackman.bean;

import java.util.Iterator;
import java.util.List;

public class Conference {
	private List<Track> tracks;

	public Conference(List<Track> tz) {
		super();
		this.tracks = tz;
	}

	public int size() {
		return tracks.size();
	}

	public boolean isEmpty() {
		return tracks.isEmpty();
	}

	public boolean add(Track e) {
		return tracks.add(e);
	}

	public Iterator<Track> iterator() {
		return tracks.iterator();
	}
	public int calculateVolume(){
		int total = 0;
		if(tracks != null) {
			for (Track t : tracks) {
				total += t.calculateVolume();
			}
		}
		return total;
	}
	public int calculateRemainingSpace(){
		int total = 0;
		if(tracks != null) {
			for (Track t : tracks) {
				total += t.calculateRemainingSpace();
			}
		}
		return total;
	}
	public String toString() {
		
		return "conf(#" 
				+ Integer.toHexString(this.hashCode()).toUpperCase()
				+ ",["+(this.tracks==null?null:this.tracks.size())+"]"
				+ ",remaining="+calculateRemainingSpace()
				+ ",volume="+calculateVolume()
				+")";
	}
}
