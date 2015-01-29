package com.twiw.trackman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
	private static final int MAXSESSIONCOUNT_PERTRACK = 2;
	Conference cfe;
			
	public Conference getResultContainers(){
		return cfe;
	}
	public void pack(Talk tk, int[] volumesInMin){
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(tk);
		this.pack(talks, volumesInMin);
	}
	class Context{
		Conference conf;
		Track day;
		Session sess;
		SessionFactory sf;
		
		public void createSessionAndDayIfNeeded(){
			this.sess = this.sf.create();
			if(this.day.getSessionCount() == MAXSESSIONCOUNT_PERTRACK){
				this.day = new Track(new ArrayList<Session>());
				this.conf.add(this.day);
			}
			this.day.add(this.sess);
		}
	}
	public void pack(List<Talk> given, int[] volumesInMin){
		
		List<Talk> sortedTalks = new ArrayList<Talk>();
		sortedTalks.addAll(given);
		Collections.sort(sortedTalks, DESCENDING_COMPARATOR);
		
		Context ctx = new Context();
		ctx.sf 		= new SessionFactory(volumesInMin);
		ctx.conf 	= new Conference(new ArrayList<Track>());
		ctx.day 	= new Track(new ArrayList<Session>());
		
		ctx.conf.add(ctx.day);
		ctx.day.add(ctx.sess = ctx.sf.create());
		System.out.println("allocating["+sortedTalks.size()+"]{="+Arrays.toString(sortedTalks.toArray())+"}");
		this.allocate(ctx, sortedTalks, 0, Integer.MAX_VALUE);
		
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
					System.out.println("searching.");
					int subAllocCount = allocate(ctx, sortedTalks, i, ctx.sess.getRemainingSpace());
					allocCount += subAllocCount;
					System.out.println("complete, "+subAllocCount+" allocated.");
					ctx.createSessionAndDayIfNeeded();
				}
			} else {
				ctx.createSessionAndDayIfNeeded();
			}
			allocCount++;
			allocVol += t.getValue();
			t.setAllocated(true);
			ctx.sess.add(t);
			System.out.println("#"+allocCount+"."+ t + " allocated in " + ctx.sess);
			
		}
		return allocCount;
	}
	 
}
