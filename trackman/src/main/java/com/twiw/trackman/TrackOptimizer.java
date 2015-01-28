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
            return t.getValue() - t1.getValue();
        }
    };
	Conference cfe;
			
	public Conference getResultContainers(){
		return cfe;
	}
	public void pack(Talk tk, int[] volumesInMin){
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(tk);
		this.pack(talks, volumesInMin);
	}
	
	public void pack(List<Talk> given, int[] volumesInMin){
		List<Talk> talks = new ArrayList<Talk>();
		talks.addAll(given);
		Collections.sort(talks, DESCENDING_COMPARATOR);
		Session sess = null;
		SessionFactory sf = new SessionFactory(volumesInMin);
		Conference ctemp = new Conference(new ArrayList<Track>());
		Track day = new Track(new ArrayList<Session>());
		ctemp.add(day);
		day.add(sess = sf.create());
		for (Talk t : talks) {
			boolean addMore = sess.hasEnoughSpace(t);
			if(addMore){
				sess.add(t);
			} else {
				sess = sf.create();
				if(day.size()==2){
					day = new Track(new ArrayList<Session>());
					ctemp.add(day);
				} else {
					sess.add(t);
				}
				day.add(sess);
			}
		}
		this.cfe = ctemp;
	}
}
