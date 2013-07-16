package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ConfigData extends PeaxyBaseDomain {
	private ConfigDataType type;

	private int version;

	public void setSchemaVersion(int schemaVersion) {

	}

	@JsonIgnore
	public ConfigDataType getType() {
		return type;
	}

	public void setType(ConfigDataType type) {
		this.type = type;
	}

	/**
	 * This is runtime version of the instance, should be incremented on every
	 * update. To be kept in sync with dataVersionMap for maintaining the data
	 * consistency in all nodes of the cluster. As XMLRootElement is annotated
	 * at the derived class need to have the version field in every config
	 * subclass
	 */
	@JsonIgnore
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void incrementVersion() {
		version++;
	}
}
