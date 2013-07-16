package net.peaxy.simulator.entity.domain;

public class JsonKeyValue extends ConfigData {
	public static final int SCHEMA_VERSION = 1;
	private String key;
	private String value;
	
	public JsonKeyValue() {
		setType(ConfigDataType.KVCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
