package net.peaxy.simulator.entity.domain;

import java.util.HashMap;
import java.util.Set;

public class DataVersionMap extends ConfigData {
	/**
	 * This is schema version of the class/dto. Needs to be incremented when
	 * class fields are changed in next SW version.
	 */
	public static final int SCHEMA_VERSION = 1;

	private HashMap<ConfigDataType, Integer> dvMap;

	public DataVersionMap() {
		setType(ConfigDataType.DataVersionMap);
		setSchemaVersion(SCHEMA_VERSION);
		dvMap = new HashMap<ConfigDataType, Integer>();
		dvMap.put(ConfigDataType.DataVersionMap, 0);
	}

	/**
	 * API used by MSP to set both the map and data version
	 * 
	 * @param mapVersion
	 * @param type
	 * @param dataVersion
	 */
	public void update(int mapVersion, ConfigDataType type, int dataVersion) {
		setVersion(mapVersion);
		dvMap.put(type, dataVersion);
		dvMap.put(ConfigDataType.DataVersionMap, mapVersion);
	}

	public synchronized int updateVersion(ConfigDataType type, int ver) {
		dvMap.put(type, ver);
		incrementVersion();
		dvMap.put(ConfigDataType.DataVersionMap, getVersion());
		return ver;
	}

	public Set<ConfigDataType> getTypes() {
		return dvMap.keySet();
	}

	public Integer getTypeVersion(ConfigDataType key) {
		return dvMap.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataVersionMap [version=").append(getVersion())
				.append(", dvMap=");
		for (ConfigDataType t : dvMap.keySet()) {
			sb.append("\n").append(t).append(" ver=").append(dvMap.get(t));
		}
		sb.append(" ]");
		return sb.toString();
	}
}
