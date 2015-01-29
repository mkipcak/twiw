package com.twiw.trackman.bean;

public class Talk {
	private boolean lightining;
	private String title;
	private int value;
	private boolean allocated;
	
	public Talk(String title, int valueInMin, boolean lightining) {
		super();
		this.value = valueInMin;
		this.title = title;
		this.lightining = lightining;
	}

	public int getValue() {
		return value;
	}

	public boolean isAllocated() {
		return allocated;
	}

	public void setAllocated(boolean allocated) {
		this.allocated = allocated;
	}
	
	public String toString() {
		return "talk(#" + Integer.toHexString(this.hashCode()).toUpperCase() 
				+ ",val="+this.getValue()
				+ ",title="+this.getTitle()
				+")";
	}

	public String getTitle() {
		return title;
	}
	public boolean isLightining() {
		return lightining;
	}
}
