package com.twiw.trackman.bean;

public class Talk {
	private String title;
	private int value;
	private boolean allocated;
	public Talk(int valueInMin) {
		super();
		this.value = valueInMin;
	}
	public Talk(String title, int valueInMin) {
		this(valueInMin);
		this.title = title;
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
		return "talk(#" + Integer.toHexString(this.hashCode()).toUpperCase() + ",val="+this.getValue()+")";
	}

	public String getTitle() {
		return title;
	}
}
