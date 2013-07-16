/**
 * 
 */
package net.peaxy.simulator.entity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liang
 *
 */
public final class SessionCacheManager {
	private static final ConcurrentHashMap<String, SessionCache> sessionCacheMap = new ConcurrentHashMap<String, SessionCache>();
	
	public static void register(String name, SessionCache sessionCache) {
		sessionCacheMap.put(name, sessionCache);
	}
	
	public static SessionCache unregister(String name) {
		return sessionCacheMap.remove(name);
	}
	
	public static SessionCache getSessionCache(String name) {
		return sessionCacheMap.get(name);
	}
	
	private SessionCacheManager() {}
}
