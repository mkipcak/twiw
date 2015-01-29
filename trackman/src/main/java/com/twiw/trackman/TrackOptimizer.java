package com.twiw.trackman;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.twiw.trackman.bean.Conference;
import com.twiw.trackman.bean.Session;
import com.twiw.trackman.bean.Talk;
import com.twiw.trackman.bean.Track;

public class TrackOptimizer {
	static final int MAXSESSIONCOUNT_PERTRACK = 2;
	public static final Comparator<Talk> DESCENDING_COMPARATOR = new Comparator<Talk>() {
        public int compare(Talk t, Talk t1) {
            return t1.getValue() - t.getValue();
        }
    };
	private Conference cfe;

	public Conference getResultContainers(){
		return cfe;
	}
	public void pack(Talk tk, int[] volumesInMin, TalkBuilder builder){
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(tk);
		this.pack(talks, volumesInMin, builder);
	}
	public void pack(List<Talk> given, int[] volumesInMin,TalkBuilder builder){

		List<Talk> sortedTalks = new ArrayList<Talk>();
		sortedTalks.addAll(given);
		Collections.sort(sortedTalks, DESCENDING_COMPARATOR);
		
		Context ctx = new Context(builder,new SessionFactory(volumesInMin),new Conference(new ArrayList<Track>()));
		
		this.addTrack(ctx);
		ctx.getTrack().add(ctx.setSession(ctx.getSessionFactory().create()));
		
		int allocated = this.allocate(ctx, sortedTalks, 0, Integer.MAX_VALUE);
		int remaining = given.size() - allocated;
		
		debug("pack completed: " + allocated + " of " + given.size() + " talks allocated,"+remaining+ " remaining");
		
		ConferencePrinter printer = new ConferencePrinter();
		printer.print(ctx.getConference(), new PrintWriter(System.out));

		this.cfe = ctx.getConference();
	}
	
	public void addSessionIfNeededAddDay(Context ctx){
		Session previous = ctx.getSession();
		ctx.setSession(ctx.getSessionFactory().create());
		if(ctx.getTrack().getSessionCount() == MAXSESSIONCOUNT_PERTRACK){
			previous.add(ctx.getTalkBuilder().buildNoVolume(TalkBuilder.TALKTITLE_NETWORK));
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
			if(t.isAllocated()||t.getValue() > maxAllocVol) {
				continue;
			}
			if(ctx.getSession().getRemainingSpace() > 0) {
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
