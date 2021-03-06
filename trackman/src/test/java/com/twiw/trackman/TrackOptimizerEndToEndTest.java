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
		
		boolean inRange = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			Talk morningFirst = trck.getFirst().iterator().next();
			Talk morningLast = null;
			for(Talk t: trck.getFirst()){
				morningLast = t;
			}
			int result 	= morningFirst.getStartTime().compareTo("09:00AM");
			int result2 = morningLast.getStartTime().compareTo("12:00PM");
			inRange = inRange && result == 0 && result2 <= 0;
			
		}
		assertTrue("not in range",  inRange);
	}
	/*
	Afternoon sessions begin at 1pm and must finish in time for the networking event.
	*/
	@Test
	public void testPackAfternoonSessionsBetween1PMAndBeforeNetworkingEvent(){
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		
		boolean inRange = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			Talk afternoonFirst = trck.getLast().iterator().next();
			Talk afternoonLast = null;
			Talk networkEvent = null;
			for(Talk t: trck.getLast()){
				if(t.getTitle().equals(TalkBuilder.TALKTITLE_NETWORK)){
					networkEvent = t;
				} else {
					afternoonLast = t;
				}
			}
			int result 	= afternoonFirst.getStartTime().compareTo("01:00PM");
			int result2 = afternoonLast.getStartTime().compareTo(networkEvent.getStartTime());
			inRange = inRange && result == 0 && result2 < 0;
			
		}
		assertTrue("not in range",  inRange);
	}
	/*
	The networking event can start no earlier than 4:00 and no later than 5:00.
	*/
	@Test
	public void testPackNetworkingEventIsBetween400and500(){
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		
		boolean inRange = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			Talk networkEvent = null;
			for(Talk t: trck.getLast()){
				networkEvent = t;
			}
			int result 	= networkEvent.getStartTime().compareTo("04:00PM");
			int result2 = networkEvent.getStartTime().compareTo("05:00PM");
			inRange = inRange && result >= 0 && result2 <= 0;
			
		}
		assertTrue("not in range",  inRange);
	}
	/*
	Presenters will be very punctual; there needs to be no gap between sessions.
	*/
	@Test
	public void testPackNoGapBetweenSessions(){
		//arrange
		TalkBuilder builder = new TalkBuilder();
		int[] volumesInMin 	= new int[]{ 3*60, 4*60 };
		List<Talk> talks 	= builder.buildAll(this.rawTalks);
		TrackOptimizer to 	= new TrackOptimizer();
		//act
		to.pack(talks, volumesInMin, builder, new String[]{"09:00AM","01:00PM"});
		//assert
		Conference cnfe    = to.getResultContainers();
		
		boolean matches = !cnfe.isEmpty(); 
		for(Track trck: cnfe) {
			for(Session sess: trck){
				int usedSpace1 = sess.getVolume() - sess.getRemainingSpace();
				int usedSpace2 = 0;
				for(Talk t: sess){
					usedSpace2 += t.getValue();
				}
				matches = matches && usedSpace1 == usedSpace2;
			}
		}
		assertTrue("gap found",  matches);
	}
}
