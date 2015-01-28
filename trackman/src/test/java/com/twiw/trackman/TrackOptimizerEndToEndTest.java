package com.twiw.trackman;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;

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
	public void testTalkBuilderWorksFineForFormatXXXNNmin() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		//act
		Talk tk = builder.build("Writing Fast Tests Against Enterprise Rails 60min");
		//assert
		assertTrue("build failed!", tk.getValue() == 60);
	}
	@Test
	public void testOptimizerAcceptsProperSingleLine() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to = new TrackOptimizer();
		//act
		Talk tk = builder.build("Writing Fast Tests Against Enterprise Rails 60min");
		to.pack(tk);
		//assert
		assertTrue("hasNoResult", to.getResultContainers() != null);
	}
}
