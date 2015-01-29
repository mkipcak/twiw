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
		
		this.allocate(ctx, sortedTalks, 0);
		
		this.cfe = ctx.conf;
	}
	private int allocate(Context ctx, List<Talk> sortedTalks, int startIndex) {
		int allocatedCount = 0;
		for (int i = startIndex; i < sortedTalks.size(); i++) {
			Talk t = sortedTalks.get(i);
			if(t.isAllocated()) {
				continue;
			}
			boolean fits = ctx.sess.hasEnoughSpace(t);
			if(!fits) {
				
				ctx.sess = ctx.sf.create();
				if(ctx.day.getSessionCount() == MAXSESSIONCOUNT_PERTRACK){
					ctx.day = new Track(new ArrayList<Session>());
					ctx.conf.add(ctx.day);
				}
				ctx.day.add(ctx.sess);
			}
			ctx.sess.add(t);
		}
		return allocatedCount;
	}
}
