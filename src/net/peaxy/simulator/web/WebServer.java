/**
 * 
 */
package net.peaxy.simulator.web;


import java.io.IOException;
import java.util.Vector;

import net.peaxy.simulator.entity.SessionCache;
import net.peaxy.simulator.entity.SessionCacheManager;
import net.peaxy.simulator.web.event.EventNotification;
import net.peaxy.simulator.web.event.EventNotifier;
import net.peaxy.simulator.web.event.serversendevents.EventNotifierServerSendEvents;
import net.peaxy.simulator.web.event.websocket.EventNotifierWebSocketApplication;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/**
 * @author Liang
 *
 */
public final class WebServer extends HttpServer implements WebServerEventAgent, WebServerObservable, SessionCache {
	private static final String webServicePackage = "net.peaxy.simulator.web.service";
	private static final String serverSendEventsPackage = "net.peaxy.simulator.web.event.serversendevents";
	private static final String networkListenerName = "peaxy.storage.simulator";
	
	private final NetworkListener listener;
	private final HttpHandler handler;
	private final EventNotifier eventNotifier;
	private final WebServerSessionManager sessionManager;
	private final String sessionRegisterName;
	private final Vector<WebServerObserver> observers;
	private final Object observersLock;
	
	public WebServer(String host, int port, String root, boolean websocket) throws Throwable {
		this.listener = new NetworkListener(networkListenerName, host, port);
		ResourceConfig resourceConfig;
		if (websocket) {
			resourceConfig = new PackagesResourceConfig(webServicePackage);
			this.listener.registerAddOn(new WebSocketAddOn());
			EventNotifierWebSocketApplication webSocketApplication = new EventNotifierWebSocketApplication();
			webSocketApplication.setWebServerEventAgent(this);
			WebSocketEngine.getEngine().register(webSocketApplication);
			this.eventNotifier = webSocketApplication;
		} else {
			resourceConfig = new PackagesResourceConfig(webServicePackage, serverSendEventsPackage);
			this.eventNotifier = EventNotifierServerSendEvents.getEventNotifier();
		}
		this.handler = ContainerFactory.createContainer(HttpHandler.class, resourceConfig);
		if (this.handler instanceof WebServerContainer) {
			WebServerContainer webServerContainer = (WebServerContainer)this.handler;
			this.sessionManager = WebServerContainer.getSessionManager();
			webServerContainer.setWebServerEventAgent(this);
		} else
			this.sessionManager = new WebServerSessionManager();
		this.getServerConfiguration().addHttpHandler(this.handler, root);
		this.sessionRegisterName = host + port;
		this.observers = new Vector<WebServerObserver>();
		this.observersLock = new Object();
		SessionCacheManager.register(this.sessionRegisterName, this);
	}

	@Override
	public void start() throws IOException {
		this.addListener(this.listener);
		super.start();
	}
	
	public synchronized void notify(EventNotification notification) {
		if (this.isStarted())
			this.eventNotifier.notify(notification);
	}
	
	public String getSessionData(String sessionID,String key) {
		return this.sessionManager.getData(sessionID,key);
	}
	
	public void setSessionData(String sessionID, String key,String value) {
		this.sessionManager.setData(sessionID, key,value);
	}
	
	public void stopSession(String sessionID) {
		this.sessionManager.stop(sessionID);
	}
	
	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.web.WebServerObservable#add(net.peaxy.storage.simulator.web.WebServerObserver)
	 */
	public void addObserver(WebServerObserver observer) {
		if (observer == null) throw new NullPointerException();
		synchronized (this.observersLock) {
			if (!this.observers.contains(observer))
				this.observers.addElement(observer);
		}
	}

	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.web.WebServerObservable#clear()
	 */
	public void clearObservers() {
		synchronized (this.observersLock) {
			this.observers.removeAllElements();
		}
	}

	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.web.WebServerObservable#notifyObservers(net.peaxy.storage.simulator.web.WebServerObserverData)
	 */
	public void notifyObservers(final WebServerEvent webServerEvent) {
		new Thread() {
			@Override
			public void run() {
				WebServerObserver[] obs;
				synchronized (WebServer.this.observersLock) {
					obs = WebServer.this.observers.toArray(new WebServerObserver[0]);
				}
				for (WebServerObserver observer : obs)
					observer.update(WebServer.this, webServerEvent);
			}
		}.start();
	}

	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.web.WebServerObservable#remove(net.peaxy.storage.simulator.web.WebServerObserver)
	 */
	public void removeObserver(WebServerObserver observer) {
		synchronized (this.observersLock) {
			this.observers.removeElement(observer);
		}
	}

	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.web.WebServerEventAgent#act(net.peaxy.storage.simulator.web.WebServerEvent)
	 */
	public void act(WebServerEvent webServerEvent) {
		this.notifyObservers(webServerEvent);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (this.isStarted())
			this.stop();
		this.getServerConfiguration().removeHttpHandler(this.handler);
		this.handler.destroy();
		SessionCacheManager.unregister(this.sessionRegisterName);
		if (this.eventNotifier instanceof EventNotifierWebSocketApplication) {
			EventNotifierWebSocketApplication webSocketApplication = (EventNotifierWebSocketApplication)this.eventNotifier;
			WebSocketEngine.getEngine().unregister(webSocketApplication);
			webSocketApplication.clearWebServerEventAgent();
		}
		this.clearObservers();
	}
}
