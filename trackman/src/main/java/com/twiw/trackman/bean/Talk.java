package com.twiw.trackman.bean;

public class Talk {
	private int value;
	private boolean allocated;
	public Talk(int valueInMin) {
		super();
		this.value = valueInMin;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isAllocated() {
		return allocated;
	}

	public void setAllocated(boolean allocated) {
		this.allocated = allocated;
	}
}
