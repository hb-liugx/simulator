/**
 * 
 */
package net.peaxy.simulator.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author Liang
 *
 */
public class PredefinedEvent implements Serializable {
	private static final long serialVersionUID = 6466010731391928566L;
	private static final String dataIdentity = "__data_internal__";
	
	private static final HashMap<String, PredefinedEvent> predefinedEvents = new HashMap<String, PredefinedEvent>();
	/**
	 * TODO this static block is for testing and debuging, it should be removed one day
	 */
	static {
		PredefinedEvent e = new PredefinedEvent();
		e.setEvent("performance.stats.realtime");
		e.setID("001");
		e.setData((new Date()).getTime());
		predefinedEvents.put("performance.stats.realtime", e);
	}
	
	public static synchronized void add(PredefinedEvent predefinedEvent) {
		predefinedEvents.put(predefinedEvent.getEvent(), predefinedEvent);
	}
	
	public static synchronized PredefinedEvent remove(PredefinedEvent predefinedEvent) {
		return predefinedEvents.get(predefinedEvent.getEvent());
	}
	
	public static synchronized PredefinedEvent get(String event) {
		return predefinedEvents.get(event);
	}
	
	public static synchronized boolean isSupported(String event) {
		return predefinedEvents.containsKey(event);
	}
	
	private String event;
	private String id;
	private HashMap<String, Object> data;
	private HashMap<String, DataRange> range;
	
	public PredefinedEvent() {
		this.data = new HashMap<String, Object>();
		this.range = new HashMap<String, DataRange>();
	}
	
	public String getEvent() {
		return this.event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setData(String name, double data) {
		this.data.put(name, data);
	}
	
	public void setData(String name, double data, double min, double max) {
		this.data.put(name, data);
		this.range.put(name, new DataRange(min, max));
	}
	
	public void setData(String name, String data) {
		this.data.put(name, data);
	}
	
	public void setData(double data) {
		this.data.clear();
		this.range.clear();
		this.setData(dataIdentity, data);
	}
	
	public void setData(double data, double min, double max) {
		this.data.clear();
		this.range.clear();
		this.setData(dataIdentity, data, min, max);
	}
	
	public void setData(String data) {
		this.data.clear();
		this.range.clear();
		this.setData(dataIdentity, data);
	}
	
	public Object getData(String name) {
		Object result = this.data.get(name);
		DataRange dataRange = this.range.get(name);
		if (dataRange != null)
			result = Double.valueOf(Math.random() * (dataRange.max - dataRange.min) + dataRange.min);
		return result;
	}
	
	public Object getData() {
		return this.getData(dataIdentity);
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("event", this.event);
			json.put("id", this.id);
			if (this.data.containsKey(dataIdentity))
				json.put("data", this.getData(dataIdentity));
			else {
				JSONObject o = new JSONObject();
				Set<String> keys = this.data.keySet();
				for (String key : keys)
					o.put(key, this.getData(key));
				json.put("data", o.toString());
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return json.toString();
	}
	
	private class DataRange implements Serializable {
		private static final long serialVersionUID = -5969818700727739326L;
		
		double min;
		double max;
		
		DataRange(double min, double max) {
			this.min = min;
			this.max = max;
		}
	}
}
