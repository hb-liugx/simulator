/**
 * 
 */
package net.peaxy.simulator.web;

/**
 * @author Liang
 *
 */
public interface WebServerObservable {
	void addObserver(WebServerObserver observer);
	void removeObserver(WebServerObserver observer);
	void clearObservers();
	void notifyObservers(final WebServerEvent webServerEvent);
}
