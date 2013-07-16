package net.peaxy.simulator.manager;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.ConfigData;
import net.peaxy.simulator.entity.domain.ConfigDataType;
import net.peaxy.simulator.entity.domain.JsonKeyValue;
import net.peaxy.simulator.entity.domain.PeaxyBaseDomain;
import net.peaxy.simulator.util.Utility;
import net.peaxy.simulator.web.exception.InternalException;

public class ConfigManager {
	
	private static ConfigManager instance = null;
	private static final String TABLE_NAME = "config";
	private static final String MESSAGE_NAME = "message";
	
	static {
		if(!TableSpace.getInstance().hasTable(TABLE_NAME)){
    		TableSpace.getInstance().addTable(TABLE_NAME);
    	}
	}
	
	public static ConfigManager getInstance(){
		if(instance == null){
			instance = new ConfigManager();
		}
		return instance;
	}
	
	private ConfigManager(){
	}
	
	public String getMessage(String id){
    	JSONObject json = TableSpace.getInstance().getData(TABLE_NAME, MESSAGE_NAME).toJson();
        try {
        	return json.getString(id);
		} catch (JSONException e) {
			throw new InternalException("can't get this message.");
		}
	}
	
	public static void addMessage(String message){
		TableSpace.getInstance().setData(TABLE_NAME, MESSAGE_NAME, Utility.getMessageID(), message);
	}
	
	public void configure(ConfigData data){
		ConfigManager.addMessage("configure data type : " + data.getType().name());
		TableSpace.getInstance().setData(TABLE_NAME,
    			data.getType().toString(), data.toJSON());
	}
	
	public ConfigData get(ConfigDataType type){
		JSONObject json = TableSpace.getInstance().getData(TABLE_NAME, type.toString()).toJson();
    	return (ConfigData)PeaxyBaseDomain.toDomain(json.toString());
	}
	
	public void configure(JsonKeyValue kv){
		ConfigManager.addMessage("configure data type : " + kv.getType().name());
		TableSpace.getInstance().setData(TABLE_NAME,
				kv.getType().toString(), kv.getKey(),kv.getValue());
	}
	
	public void configure(String key,String value){
		JsonKeyValue kv = new JsonKeyValue();
		kv.setKey(key);
		kv.setValue(value);
		configure(kv);
	}
	
	public JsonKeyValue get(String key) throws Exception{
		JSONObject json = TableSpace.getInstance().getData(TABLE_NAME, ConfigDataType.KVCfg.toString()).toJson();
		JsonKeyValue kv = new JsonKeyValue();
    	try {
    		kv.setKey(key);
    		kv.setValue(json.getString(key));
    		return kv;
		} catch (JSONException e) {
			throw new Exception("get jsonkeyvalue object error " + e.getMessage());
		}
	}
}
