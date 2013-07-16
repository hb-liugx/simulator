/**
 * 
 */
package net.peaxy.simulator.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liang
 *
 */
public final class WebServerSessionManager {
	private static final long timeout = 1800000L;
	
	private ConcurrentHashMap<String, Session> sessions;
	private Timer clearTimer;

	public WebServerSessionManager() {
		this.sessions = new ConcurrentHashMap<String, Session>();
		long delay = timeout / 3;
		this.clearTimer = new Timer();
		this.clearTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				clear();
			}
		}, delay, delay);
	}
	
	public void start(String sessionID) {
		if (this.sessions.containsKey(sessionID))
			 this.sessions.get(sessionID).last = (new Date()).getTime();
		else
			this.sessions.put(sessionID, new Session());
	}
	
	public void stop(String sessionID) {
		this.sessions.remove(sessionID);
	}
	
	public void setData(String sessionID, String key,String value) {
		Session session = this.sessions.get(sessionID);
		if (session != null){
			if(session.data == null){
				session.data = new HashMap<String, String>();
			}
			session.data.put(key, value);
		}
	}
	
	public String getData(String sessionID,String key) {
		String value = null;
		Session session = this.sessions.get(sessionID);
		if(session != null && session.data != null){
			value = session.data.get(key);
		}
		return value;
	}
	
	public void refresh(String sessionID) {
		Session session = this.sessions.get(sessionID);
		if (session != null)
			session.last = (new Date()).getTime();
		else
			this.sessions.put(sessionID, new Session());
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.clearTimer.cancel();
		super.finalize();
	}
	
	private void clear() {
		long current = (new Date()).getTime();
		Set<String> keys = this.sessions.keySet();
		Session session;
		for (String key : keys) {
			session = this.sessions.get(key);
			if (session != null) {
				if (current - session.last > timeout)
					this.sessions.remove(key);
			}
		}
	}
	
	private class Session {
		private long start;
		private long last;
		private HashMap<String,String> data;
		
		public Session() {
			this.start = (new Date()).getTime();
			this.last = this.start;
			data = new HashMap<String, String>();
		}
	}
}
