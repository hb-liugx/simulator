package net.peaxy.simulator.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Record implements Serializable {

	private static final long serialVersionUID = 2988605019503475940L;

	private HashMap<String, String> datas;

	Record() {
		datas = new HashMap<String, String>();
		//datas.put("version", "1");
		//datas.put("schema_version", "1");
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		synchronized (datas) {
			Set<String> keys = datas.keySet();
			for (String key : keys) {
				try {
					json.put(key, datas.get(key));
				} catch (JSONException e) {
				}
			}
		}
		return json;
	}

	String getDataValue(String key) {
		String r = null;
		synchronized (datas) {
			r = datas.get(key);
		}
		return r;
	}

	public void setDataValue(String key, String data) {
		synchronized (datas) {
			datas.put(key, data);
		}
	}

	boolean hasKey(String key) {
		boolean flag = false;
		synchronized (datas) {
			flag = datas.containsKey(key);
		}
		return flag;
	}

}
