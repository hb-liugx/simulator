package net.peaxy.simulator.entity.domain;

import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.jackson.annotate.JsonProperty;

public class ClusterCfg extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	private static AtomicReference<ClusterCfg> instance;

	private String clusterName;

	private String managementIp;

	private int clusterId;

	static {
		instance = new AtomicReference<ClusterCfg>();
	}

	public ClusterCfg() {
		setType(ConfigDataType.ClusterCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	public static ClusterCfg getInstance() {
		return instance.get();
	}

	public static void setInstance(ClusterCfg s) {
		instance.set(s);
	}

	@JsonProperty("cluster_name")
	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	@JsonProperty("management_ip")
	public String getManagementIp() {
		return managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	@JsonProperty("cluster_id")
	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	@Override
	public String toString() {
		return "ClusterCfg [clusterName=" + clusterName + ", managementIp="
				+ managementIp + ", clusterId=" + clusterId + "]";
	}
}
