package com.twiw.trackman.bean;

import junit.framework.TestCase;

import org.junit.Test;

import com.twiw.trackman.SessionFactory;

public class SessionFactoryTests extends TestCase{

	@Test
	public void testMorningValumeIsValid() {
		//arrange
		SessionFactory sf = new SessionFactory(new int[]{3*60,4*60});
		//act
		Session sess = sf.create();
		//assert
		assertTrue("volume invalid",sess.getVolume() == 3*60);
	}
	@SuppressWarnings("unused")
	@Test
	public void testAfternoonVolumeIsValid() {
		//arrange
		SessionFactory sf = new SessionFactory(new int[]{3*60,4*60});
		//act
		Session sessMorng = sf.create();
		Session sessAftNn = sf.create();
		//assert
		assertTrue("volume invalid",sessAftNn.getVolume() == 4*60);
	}
	@SuppressWarnings("unused")
	@Test
	public void testSecondDayMorningValumeIsValid() {
		//arrange
		SessionFactory sf = new SessionFactory(new int[]{3*60,4*60});
		//act
		Session sessMorng = sf.create();
		Session sessAftNn = sf.create();
		Session sessNextMorng = sf.create();
		//assert
		assertTrue("volume invalid",sessNextMorng.getVolume() == 3*60);
	}	
	@SuppressWarnings("unused")
	@Test
	public void testSecondDayAfternoonValumeIsValid() {
		//arrange
		SessionFactory sf = new SessionFactory(new int[]{3*60,4*60});
		//act
		Session sessMorng = sf.create();
		Session sessAftNn = sf.create();
		Session sessNextMorng = sf.create();
		Session sessNextAftNn = sf.create();
		//assert
		assertTrue("volume invalid",sessNextAftNn.getVolume() == 4*60);
	}
}
