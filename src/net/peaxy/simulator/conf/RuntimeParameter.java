/**
 * 
 */
package net.peaxy.simulator.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * @author Liang
 *
 */
public final class RuntimeParameter {
	private static HashMap<String, String> configuration = new HashMap<String, String>();
	
	static {
		configuration.put("simulator.web.host", "192.168.3.112");
		configuration.put("simulator.web.port", "8080");
		configuration.put("simulator.web.path", "/");
		configuration.put("simulator.web.root", "web");
		configuration.put("simulator.web.default", "index.html");
		configuration.put("simulator.web.websocket", "true");
	}
	
	public static synchronized void load(String cfgFileName) {
		Properties prop = new Properties();
		
		try {
			InputStream file = new FileInputStream(cfgFileName);
			prop.load(file);
			Set<Object> keys = prop.keySet();
			for(Object key : keys){
				configuration.put(key.toString(), String.valueOf(prop.get(key)));
			}
			file.close();
		} catch (IOException e) {
		}
		
	}
	
	public static synchronized void set(String name, String value) {
		configuration.put(name, value);
	}
	
	public static synchronized String get(String name, String defaultValue) {
		String value = configuration.get(name);
		if (value != null)
			return configuration.get(name);
		return defaultValue;
	}
	
	public static synchronized int get(String name, int defaultValue) {
		int result = defaultValue;
		String value = configuration.get(name);
		if (value != null) {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException e) {}
		}
		return result;
	}
	
	public static synchronized boolean get(String name, boolean defaultValue) {
		boolean result = defaultValue;
		String value = configuration.get(name);
		if (value != null)
			result = "true".equalsIgnoreCase(value);
		return result;
	}
	
	private RuntimeParameter() {}
}
