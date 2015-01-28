package com.twiw.trackman;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;
import com.twiw.trackman.bean.Track;

public class TrackOptimizerEndToEndTest extends TestCase {
	String rawTalks;
	@Before
	protected void setUp() throws Exception {
		this.rawTalks = "Writing Fast Tests Against Enterprise Rails 60min"+
						"Overdoing it in Python 45min"+
						"Lua for the Masses 30min"+
						"Ruby Errors from Mismatched Gem Versions 45min"+
						"Common Ruby Errors 45min"+
						"Rails for Python Developers lightning"+
						"Communicating Over Distance 60min"+
						"Accounting-Driven Development 45min"+
						"Woah 30min"+
						"Sit Down and Write 30min"+
						"Pair Programming vs Noise 45min"+
						"Rails Magic 60min"+
						"Ruby on Rails: Why We Should Move On 60min"+
						"Clojure Ate Scala (on my project) 45min"+
						"Programming in the Boondocks of Seattle 30min"+
						"Ruby vs. Clojure for Back-End Development 30min"+
						"Ruby on Rails Legacy App Maintenance 60min"+
						"A World Without HackerNews 30min"+
						"User Interface CSS in Rails Apps 30min";
	}
	
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testPack0MinVolumeFor1MinTalk() {
		//arrange
		int[] volumesInMin 	= new int[]{ 1 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		Talk tk = builder.build("xxx 1min");
		to.pack(tk, volumesInMin);
		//assert
		Conference cnfe = to.getResultContainers();
		assertTrue("hasNoResult", cnfe.size() == 1);
		Track day 		= cnfe.iterator().next();
		Session morning = day.iterator().next();
		assertTrue("remaining space is miscalculated", morning.getRemainingSpace()==0);
	}
	@Test
	public void testPack1MinVolumeFor1MinTalk() {
		//arrange
		int[] volumesInMin 	= new int[]{ 1 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		Talk tk = builder.build("xxx 1min");
		to.pack(tk, volumesInMin);
		//assert
		Conference cnfe = to.getResultContainers();
		assertTrue("hasNoResult", cnfe.size() == 1);
		Track day 		= cnfe.iterator().next();
		Session morning = day.iterator().next();
		assertTrue("remaining space is miscalculated", morning.getRemainingSpace()==0);
	}
	@Test
	public void testPack_3MinVolume_To_1MinTalk() {
		//arrange
		int[] volumesInMin 	= new int[]{ 3 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		Talk tk = builder.build("xxx 1min");
		to.pack(tk, volumesInMin);
		//assert
		Conference cnfe = to.getResultContainers();
		assertTrue("hasNoResult", cnfe.size() == 1);
		Track day 		= cnfe.iterator().next();
		Session morning = day.iterator().next();
		assertTrue("remaining space is miscalculated", morning.getRemainingSpace()==2);
	}
	@Test
	public void testPack_2MinVolume_To_2X_1MinTalk() {
		//arrange
		int[] volumesInMin 	= new int[]{ 1, 1 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		List<Talk> talks = builder.buildAll("xxx 1min\nyyy 1min\n");
		to.pack(talks, volumesInMin);
		//assert
		Conference cnfe = to.getResultContainers();
		assertTrue("hasNoResult", cnfe.size() == 1);
		
		Track day 		= cnfe.iterator().next();
		Iterator<Session> dayIterator = day.iterator();
		Session morning = dayIterator.next();
		Session afternn = dayIterator.next();
		assertTrue("morning remaining space is miscalculated", morning.getRemainingSpace()==0);
		assertTrue("afternn remaining space is miscalculated", afternn.getRemainingSpace()==0);
	}
	@Test
	public void testOptimizerAcceptsProperSingleLine() {
		//arrange
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		Talk tk = builder.build("Writing Fast Tests Against Enterprise Rails 60min");
		to.pack(tk, volumesInMin);
		//assert
		assertTrue("hasNoResult", to.getResultContainers() != null);
	}
}
