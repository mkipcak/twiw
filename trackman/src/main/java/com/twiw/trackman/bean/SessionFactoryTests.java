package com.twiw.trackman.bean;

import junit.framework.TestCase;

import org.junit.Test;

import com.twiw.trackman.SessionFactory;

public class SessionFactoryTests extends TestCase{

	@Test
	public void test() {
		//arrange
		SessionFactory sf = new SessionFactory();
		//act
		Session sess = sf.create();
		//assert
		assertTrue("volume invalid",sess.getVolume() == 3*60);
	}

}
