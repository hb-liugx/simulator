package net.peaxy.simulator.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DataTable implements Serializable {

	private static final long serialVersionUID = 6946514637083084946L;

	private HashMap<String,Record> records;
	
	DataTable(){
		records = new HashMap<String, Record>();
	}

	String[] allKeys(){
		String[] strs = new String[0];
		synchronized (records) {
			strs = records.keySet().toArray(new String[0]);
		}
		return strs;
	}
	
	Record getData(String key) {
		Record json;
		synchronized (records) {
			if(records.containsKey(key)){
				json = records.get(key);
			} else {
				json = new Record();
				records.put(key, json);
			}
		}
		return json;
	}
	
	Record addEmptyRecord(String tableKey){
		Record record = null;
		synchronized (records) {
			if(!records.containsKey(tableKey)){
				record = new Record();
				records.put(tableKey, record);
			}
		}
		return record;
	}
	
	void setData(String tableKey, JSONObject json) {
		String key = "";
		synchronized (records) {
			Record record = records.get(tableKey);
			if(record == null){
				record = new Record();
				records.put(tableKey, record);
			}
		
			Iterator<?> iter = json.keys();
			while(iter.hasNext()){
				key = iter.next().toString();
				try {
					record.setDataValue(key, json.get(key).toString());
					//record.put(key, json.get(key));
				} catch (JSONException e) {
				}
			}
		}
	}

	void removeData(String tableKey) {
		synchronized (records) {
			if(records.containsKey(tableKey)){
				records.remove(tableKey);
			}
		}
	}
	
	boolean hasTableKey(String tableKey) {
		return records.containsKey(tableKey);
	}
	
	boolean hasData() {
		return !records.isEmpty();
	}
}
