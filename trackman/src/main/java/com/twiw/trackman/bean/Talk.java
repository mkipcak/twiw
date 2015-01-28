package com.twiw.trackman.bean;

public class Talk {
	private int value;

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
}
