package com.twiw.trackman;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;
import com.twiw.trackman.bean.Track;

public class TrackOptimizer {

	public static final Comparator<Talk> DESCENDING_COMPARATOR = new Comparator<Talk>() {
        public int compare(Talk t, Talk t1) {
            return t1.getValue() - t.getValue();
        }
    };
	private Conference cfe;

	public Conference getResultContainers(){
		return cfe;
	}
	public void pack(Talk tk, int[] volumesInMin, TalkBuilder builder, String[] sessStartTimes){
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(tk);
		this.pack(talks, volumesInMin, builder, sessStartTimes);
	}
	public void pack(List<Talk> given, int[] volumesInMin,TalkBuilder builder, String[] sessStartTimes){

		List<Talk> sortedTalks = new ArrayList<Talk>();
		sortedTalks.addAll(given);
		Collections.sort(sortedTalks, DESCENDING_COMPARATOR);
		
		Context ctx = new Context(builder,new SessionFactory(volumesInMin),new Conference(new ArrayList<Track>()));
		
		//this.addTrack(ctx);
		//ctx.getTrack().add(ctx.setSession(ctx.getSessionFactory().create()));
		
		int allocated = this.allocate(ctx, sortedTalks, 0, Integer.MAX_VALUE);
		int remaining = given.size() - allocated;
		
		debug("pack completed: " + allocated + " of " + given.size() + " talks allocated,"+remaining+ " remaining");
		
 		this.applyTimeInterval(sessStartTimes, ctx);

		this.cfe = ctx.getConference();
	}
	public void applyTimeInterval(String[] sessStartTimes, Context ctx){
		for(Track trck: ctx.getConference()) {
			int sessIdx = 0;
			Session firstSessionOfTheDay = null;
			Session lastSessionOfTheDay = null;
			Talk lastTalkOfTheDay = null;
			
			for(Session sess: trck){
				String dtStart = sessStartTimes[sessIdx++];
				if(firstSessionOfTheDay == null){
					firstSessionOfTheDay = sess;
				}
				lastSessionOfTheDay = sess;
				lastTalkOfTheDay = applyTimeInterval(sess, lastTalkOfTheDay, dtStart, ctx);
			}

			if(firstSessionOfTheDay != null){
				addLunch(firstSessionOfTheDay, ctx);
			}
			
			if(lastSessionOfTheDay != null 
					&& lastTalkOfTheDay != null){
				addNetworkEvent(lastSessionOfTheDay, lastTalkOfTheDay, ctx);
			}
			
		}
		
	}
	private void addLunch(Session firstSessionOfTheDay, Context ctx) {
		Talk networkEvent = ctx.getTalkBuilder().buildNoVolume(TalkBuilder.TALKTITLE_LUNCH);
		networkEvent.setStartTime(ctx.lunchTime);
		firstSessionOfTheDay.add(networkEvent);
	}
	private void addNetworkEvent(Session lastSessionOfTheDay, Talk lastTalkOfTheDay, Context ctx) {
		 
		try {
			SimpleDateFormat df = new SimpleDateFormat(ctx.timeFormat);
			
			Date dt = df.parse(lastTalkOfTheDay.getStartTime());
			Calendar cldr	= GregorianCalendar.getInstance();
			cldr.setTime(dt);
			cldr.add(Calendar.MINUTE, lastTalkOfTheDay.getValue());
			
			String stNetworkStart = df.format(cldr.getTime());
			
			//improve comparisions
			int result = df.parse(stNetworkStart).compareTo(df.parse(ctx.networkEventLowValue));
            if(result < 0) {
                 stNetworkStart = ctx.networkEventLowValue;
            }
            int result2 = df.parse(stNetworkStart).compareTo(df.parse(ctx.networkEventHighValue));
            if(result2 > 0) {
                 stNetworkStart = ctx.networkEventHighValue;
            }

			Talk networkEvent = ctx.getTalkBuilder().buildNoVolume(TalkBuilder.TALKTITLE_NETWORK);
			networkEvent.setStartTime(stNetworkStart);
			lastSessionOfTheDay.add(networkEvent);
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	private Talk applyTimeInterval(Session sess, Talk lastTalkOfTheDay, String dtStart, Context ctx) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(ctx.timeFormat);
			
			Date dt 		= df.parse(dtStart);
			Calendar cldr	= GregorianCalendar.getInstance();
			cldr.setTime(dt);
			for (Talk t : sess) {
				lastTalkOfTheDay = t;
				String st = df.format(cldr.getTime());
				t.setStartTime(st);
				cldr.add(Calendar.MINUTE, t.getValue());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastTalkOfTheDay;
	}
	
	public void addSessionIfNeededAddDay(Context ctx){
		ctx.setSession(ctx.getSessionFactory().create());
		if(ctx.getTrack() == null
                || ctx.getTrack().getSessionCount() == ctx.maxSessionPerTrack){
				this.addTrack(ctx);
		}
		ctx.getTrack().add(ctx.getSession());
	}
	
	public void addTrack(Context ctx){
		ctx.setTrack(new Track(ctx.trackCounter++, new ArrayList<Session>()));
		ctx.getConference().add(ctx.getTrack());
	}
	
	private int allocate(Context ctx, List<Talk> sortedTalks, int startIndex, int maxAllocVol) {
		int allocCount = 0;
		int allocVol = 0;
		for (int i = startIndex; i < sortedTalks.size(); i++) {
			if(allocVol >= maxAllocVol) {
				break;
			}
			Talk t = sortedTalks.get(i);
            if(t.isAllocated()
                   || t.getValue() > maxAllocVol
                   || t.getValue() > ctx.getSessionFactory().findLargestVolumeInaSession()) {
                 continue;
            }
            if(ctx.getSession() != null
                   && ctx.getSession().getRemainingSpace() > 0) {
                   boolean fits = ctx.getSession().hasEnoughSpace(t);
                   if(!fits) {
                                           debug("searching,");
					int subAllocCount = allocate(ctx, sortedTalks, i, ctx.getSession().getRemainingSpace());
					allocCount += subAllocCount;
					debug("search completed, "+subAllocCount+" allocated.");
					this.addSessionIfNeededAddDay(ctx);
				}
			} else {
				this.addSessionIfNeededAddDay(ctx);
			}
			allocCount++;
			allocVol += t.getValue();
			t.setAllocated(true);
			ctx.getSession().add(t);
			debug("#"+allocCount+"."+ t + " allocated in " + ctx.getSession());
		}
		return allocCount;
	}
	private void debug(String s) {
		//System.out.println(s);
	}
	 
}
