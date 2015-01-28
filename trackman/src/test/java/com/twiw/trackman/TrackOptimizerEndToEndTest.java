package com.twiw.trackman;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void testOneConferenceOneSessionAcceptsOneTalk() {
		//arrange
		TrackOptimizer tm = new TrackOptimizer();
		//act
		tm.pack("Writing Fast Tests Against Enterprise Rails 60min");
		//assert
		assertTrue("hasNoResult", tm.getResultContainers() != null);
	}

}
