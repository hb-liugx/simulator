/**
 * 
 */
package net.peaxy.simulator.web.event.serversendevents;

import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import net.peaxy.simulator.entity.PredefinedEvent;
import net.peaxy.simulator.web.event.EventNotification;
import net.peaxy.simulator.web.event.EventNotifier;

import com.sun.jersey.spi.resource.Singleton;

/**
 * @author Liang
 *
 */
@Singleton
@Path("/event/{event}/register")
public final class EventNotifierServerSendEvents implements EventNotifier {
	private static final long timeout = 1800000L;
	private static final String eventPrefix = "data: ";
	private static EventNotifier currentInstance;
	
	public static EventNotifier getEventNotifier() {
		return currentInstance;
	}
	
	private final ConcurrentHashMap<String, EventNotification> notifications;
	private Timer clearTimer;
	
	public EventNotifierServerSendEvents() {
		this.notifications = new ConcurrentHashMap<String, EventNotification>();
		this.clearTimer = new Timer();
		long delay = timeout / 3;
		this.clearTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				clear();
			}
		}, delay, delay);
		currentInstance = this;
	}
	
	@GET
	@Produces("text/event-stream; charset=UTF-8")
	public String register(@PathParam("event") String event) {
		if (PredefinedEvent.isSupported(event)) {
			EventNotification notification = this.notifications.get(event);
			if (notification != null)
				return eventPrefix + notification.getData();
			return eventPrefix;
		}
		throw new WebApplicationException(404);
	}

	public void notify(EventNotification notification) {
		this.notifications.put(notification.getEvent(), notification);
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.clearTimer.cancel();
		super.finalize();
	}
	
	private void clear() {
		long current = (new Date()).getTime();
		Set<String> keys = this.notifications.keySet();
		for (String key : keys) {
			if (current - this.notifications.get(key).getTimestamp() > timeout)
				this.notifications.remove(key);
		}
	}
}
