package net.peaxy.simulator.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.peaxy.simulator.util.Utility;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class PeaxyDomainReader {

	private JSONObject main;
	private boolean isArray = false;
	private boolean isMap = false;
	private boolean isObject = false;

	public static void main(String[] args){
		
	}
	
	public void getDomain() throws Exception{
		if(isArray){
			JSONArray array = main.getJSONArray("array");
			List<JSONObject> list = new ArrayList<JSONObject>();
			for(int i=0;i<array.length();i++){
				list.add(array.getJSONObject(i));
			}
		} else if(isObject){
			
		} else if(isMap){
			Map<String,JSONObject> map = new HashMap<String,JSONObject>();
			JSONObject array = main.getJSONObject("map");
			JSONObject element;
			String key;
			Iterator<?> keys = array.keys();
			while(keys.hasNext()){
				key = keys.next().toString();
				element = array.getJSONObject(key);
				key = element.keys().next().toString();
				map.put(key, element.getJSONObject(key));
			}
		}
	}
	
	public JSONObject getPeaxyDomain(String filename) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(filename);
			main = new JSONObject();
			Element root = doc.getRootElement();
			if("node".equalsIgnoreCase(root.getName())){
				isObject = true;
				String className = root.getAttributeValue("class");
				JSONObject child = new JSONObject();
				try {
					main.put(className, child);
					addNode(child,root);
				} catch (JSONException e) {
				}
			} else if("array".equalsIgnoreCase(root.getName())){
				isArray = true;
				JSONArray array = new JSONArray();
				addArrayNode(array, root.getChildren());
				main = new JSONObject();
				try {
					main.put("array", array);
				} catch (JSONException e) {
				}
			} else if("map".equalsIgnoreCase(root.getName())){
				isMap = true;
				
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return main;
	}

	private void addArrayNode(JSONArray array, List<Element> children) {
		int index = 0;
		for (Element el : children) {
			String className = el.getAttributeValue("class");
			JSONObject child = new JSONObject();
			JSONObject json = new JSONObject();
			try {
				child.put(className, json);
				array.put(index, child);
				index++;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			addNode(json,el);
		}
	}

	public void addMapNode(JSONObject map, List<Element> children) {

	}
	
	private void addNode(JSONObject parent, Element root) {
		List<Element> list = root.getChildren();
		for (Element el : list) {
			if("node".equalsIgnoreCase(el.getName())){
				String className = root.getAttributeValue("class");
				JSONObject child = new JSONObject();
				try {
					parent.put(className, child);
				} catch (JSONException e) {
				}
				addNode(child,el);
			} else if("leaf".equalsIgnoreCase(el.getName())){
				String name = el.getAttributeValue("name");
				String property = el.getAttributeValue("property");
				String value = el.getText();
				if("id".equalsIgnoreCase(property)){
					value = Utility.getID()+"";
				}
				try {
					parent.put(name, value);
				} catch (JSONException e) {
				}
			} else if("array".equalsIgnoreCase(el.getName())){
				String name = el.getAttributeValue("name");
				JSONArray array = new JSONArray();
				try {
					parent.put(name, array);
				} catch (JSONException e) {
				}
				addArrayNode(array,el.getChildren());
			} else if("map".equalsIgnoreCase(el.getName())){
				String name = el.getAttributeValue("name");
				String key = el.getAttributeValue("key");
				JSONObject child = new JSONObject();
				try {
					parent.put(key+name, child);
				} catch (JSONException e) {
				}
				addNode(child,el);
			}
		}
		
	}
}