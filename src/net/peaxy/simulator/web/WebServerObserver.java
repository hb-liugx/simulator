/**
 * 
 */
package net.peaxy.simulator.web;

/**
 * @author Liang
 *
 */
public interface WebServerObserver {
	void update(WebServerObservable observable, WebServerEvent webServerEvent);
}
