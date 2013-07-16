/**
 * 
 */
package net.peaxy.simulator.entity;


/**
 * @author Liang
 *
 */
public interface SessionCache {
	String getSessionData(String sessionID,String key);
	void setSessionData(String sessionID, String key,String data);
	void stopSession(String sessionID);
}
