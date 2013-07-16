package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class DnsCfg extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	private String search1;

	private String search2;

	private String search3;

	private String server1;

	private String server2;

	private String server3;

	public DnsCfg() {
		setType(ConfigDataType.DNSCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("search1")
	public String getSearch1() {
		return search1;
	}

	public void setSearch1(String search1) {
		this.search1 = search1;
	}

	@JsonProperty("search2")
	public String getSearch2() {
		return search2;
	}

	public void setSearch2(String search2) {
		this.search2 = search2;
	}

	@JsonProperty("search3")
	public String getSearch3() {
		return search3;
	}

	public void setSearch3(String search3) {
		this.search3 = search3;
	}

	@JsonProperty("srv1")
	public String getServer1() {
		return server1;
	}

	public void setServer1(String server) {
		this.server1 = server;
	}

	@JsonProperty("srv2")
	public String getServer2() {
		return server2;
	}

	public void setServer2(String server2) {
		this.server2 = server2;
	}

	@JsonProperty("srv3")
	public String getServer3() {
		return server3;
	}

	public void setServer3(String server3) {
		this.server3 = server3;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DnsCfg [search1=").append(search1).append(", search2=")
				.append(search2).append(", search3=").append(search3)
				.append(", server1=").append(server1).append(", server2=")
				.append(server2).append(", server3=").append(server3)
				.append("]");
		return builder.toString();
	}
}
