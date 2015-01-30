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
		this.rawTalks = "Writing Fast Tests Against Enterprise Rails 60min\n"+
						"Overdoing it in Python 45min\n"+
						"Lua for the Masses 30min\n"+
						"Ruby Errors from Mismatched Gem Versions 45min\n"+
						"Common Ruby Errors 45min\n"+
						"Rails for Python Developers lightning\n"+
						"Communicating Over Distance 60min\n"+
						"Accounting-Driven Development 45min\n"+
						"Woah 30min\n"+
						"Sit Down and Write 30min\n"+
						"Pair Programming vs Noise 45min\n"+
						"Rails Magic 60min\n"+
						"Ruby on Rails: Why We Should Move On 60min\n"+
						"Clojure Ate Scala (on my project) 45min\n"+
						"Programming in the Boondocks of Seattle 30min\n"+
						"Ruby vs. Clojure for Back-End Development 30min\n"+
						"Ruby on Rails Legacy App Maintenance 60min\n"+
						"A World Without HackerNews 30min\n"+
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
		to.pack(tk, volumesInMin, builder,new String[]{"09:00AM","01:00PM"});
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
		to.pack(tk, volumesInMin, builder,new String[]{"09:00AM","01:00PM"});
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
		to.pack(tk, volumesInMin, builder,new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe = to.getResultContainers();

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
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe = to.getResultContainers();
		
		Track day 		= cnfe.iterator().next();
		Iterator<Session> dayIterator = day.iterator();
		Session morning = dayIterator.next();
		Session afternn = dayIterator.next();
		assertTrue("morning remaining space is miscalculated", morning.getRemainingSpace()==0);
		assertTrue("afternn remaining space is miscalculated", afternn.getRemainingSpace()==0);
	}
	//@Test
	public void testPackEightTalkForTwoDayVolume() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 100, 50 };
		List<Talk> talks 	= builder.buildAll(30,30,30,10,50,45,45,10);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		Iterator<Track> it = cnfe.iterator();
		Track day1 		   = it.next();
		Track day2 		   = it.next();

		assertTrue("d1Morning remaining space is miscalculated", day1.getFirst().getRemainingSpace()==5);
		assertTrue("d1Afternn remaining space is miscalculated", day1.getLast().getRemainingSpace()==5);
		
		assertTrue("d2Morning remaining space is miscalculated", day2.getFirst().getRemainingSpace()==0);
		assertTrue("d2Afternn remaining space is miscalculated", day2.getLast().getRemainingSpace()==40);
	}
	@Test
	public void testPack10TalkForTwoDayVolume() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 100, 50 };
		List<Talk> talks 	= builder.buildAll(30,30,30,10,50,45,45,10,5,5,40);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		Iterator<Track> it = cnfe.iterator();
		Track day1 		   = it.next();
		Track day2 		   = it.next();

		assertTrue("d1Morning remaining space is miscalculated", day1.getFirst().getRemainingSpace()==0);
		assertTrue("d1Afternn remaining space is miscalculated", day1.getLast().getRemainingSpace()==0);
		
		assertTrue("d2Morning remaining space is miscalculated", day2.getFirst().getRemainingSpace()==0);
		assertTrue("d2Afternn remaining space is miscalculated", day2.getLast().getRemainingSpace()==0);
	}
	@Test
	public void testPackForGivenInputs() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		Iterator<Track> it = cnfe.iterator();
		Track day1 		   = it.next();
		Track day2 		   = it.next();

		assertTrue("day1 morning is missing", day1.getFirst() != null);
		assertTrue("day1 afternn is missing", day1.getLast() != null);
		assertTrue("day2 morning is missing", day2.getFirst() != null);
		assertTrue("day2 afternn is missing", day2.getLast() != null);
	}
	@Test
	public void testOptimizerAcceptsProperSingleLine() {
		//arrange
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		TalkBuilder builder = new TalkBuilder();
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		Talk tk = builder.build("Writing Fast Tests Against Enterprise Rails 60min");
		to.pack(tk, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		assertTrue("hasNoResult", to.getResultContainers() != null);
	}
	
	/*
	The conference has multiple tracks each of which has a morning and afternoon session.
	*/
	@Test
	public void testPackEachTrackHasAMorningAndAfternoonSession() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		
		boolean eachHaveBoth = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			eachHaveBoth = eachHaveBoth && trck.size() == 2;
		}
		assertTrue("dont have both",  eachHaveBoth);
	}
	/*
	Each session contains multiple talks.
	*/
	@Test
	public void testPackEachSessionContainsMultipleTasks() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		
		boolean eachSessHaveMultipleTasks = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			for(Session sess: trck){
				eachSessHaveMultipleTasks = eachSessHaveMultipleTasks && sess.size() > 1;
			}
		}
		assertTrue("dont have both",  eachSessHaveMultipleTasks);
	}
	/*
	Morning sessions begin at 9am and must finish by 12 noon, for lunch.
	Afternoon sessions begin at 1pm and must finish in time for the networking event.
	The networking event can start no earlier than 4:00 and no later than 5:00.
	No talk title has numbers in it.
	All talk lengths are either in minutes (not hours) or lightning (5 minutes).
	Presenters will be very punctual; there needs to be no gap between sessions.
	*/
	@Test
	public void testPackMorningSessionsBetween9AMAnd12Noon(){
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		/*
		boolean inRange = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			trck.getFirst().;
			for(Session sess: trck){
				inRange = eachSessHaveMultipleTasks && sess.size() > 1;
			}
		}
		assertTrue("dont have both",  eachSessHaveMultipleTasks);*/
	}
}
