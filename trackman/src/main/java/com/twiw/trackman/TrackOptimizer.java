package com.twiw.trackman;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.twiw.trackman.bean.Conference;
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
	public void pack(Talk tk, int[] volumesInMin){
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(tk);
		this.pack(talks, volumesInMin);
	}
	public void pack(List<Talk> given, int[] volumesInMin){
		
		List<Talk> sortedTalks = new ArrayList<Talk>();
		sortedTalks.addAll(given);
		Collections.sort(sortedTalks, DESCENDING_COMPARATOR);
		
		Context ctx = new Context();
		ctx.sf 		= new SessionFactory(volumesInMin);
		ctx.conf 	= new Conference(new ArrayList<Track>());
		ctx.addTrack();
		ctx.day.add(ctx.sess = ctx.sf.create());
		
		int allocated = this.allocate(ctx, sortedTalks, 0, Integer.MAX_VALUE);
		int remaining = given.size() - allocated;
		
		debug("pack completed: " + allocated + " of " + given.size() + " talks allocated,"+remaining+ " remaining");

		ConferencePrinter printer = new ConferencePrinter();
		printer.print(ctx.conf, new PrintWriter(System.out));

		this.cfe = ctx.conf;
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
			if(ctx.sess.getRemainingSpace() > 0) {
				boolean fits = ctx.sess.hasEnoughSpace(t);
				if(!fits) {
					debug("searching,");
					int subAllocCount = allocate(ctx, sortedTalks, i, ctx.sess.getRemainingSpace());
					allocCount += subAllocCount;
					debug("search completed, "+subAllocCount+" allocated.");
					ctx.addSessionIfNeededAddDay();
				}
			} else {
				ctx.addSessionIfNeededAddDay();
			}
			allocCount++;
			allocVol += t.getValue();
			t.setAllocated(true);
			ctx.sess.add(t);
			debug("#"+allocCount+"."+ t + " allocated in " + ctx.sess);
			
		}
		return allocCount;
	}
	private void debug(String s) {
		System.out.println(s);
		
	}
	 
}
