/**
 * 
 */
package net.peaxy.simulator.web.event;

import java.io.Serializable;
import java.util.Date;

import net.peaxy.simulator.entity.PredefinedEvent;

/**
 * @author Liang
 *
 */
public class EventNotification implements Serializable {
	private static final long serialVersionUID = 6080746708433424691L;
	
	private String event;
	private String data;
	private long timestamp;
	
	public EventNotification(PredefinedEvent event) {
		this.timestamp = (new Date()).getTime();
		this.event = event.getEvent();
		this.data = event.toString();
	}
	
	public String getEvent() {
		return this.event;
	}
	
	public String getData() {
		return data;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
}
