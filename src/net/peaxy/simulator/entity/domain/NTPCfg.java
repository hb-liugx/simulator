package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class NTPCfg extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	private String timeZone;

	private String ntpServer1;
	// @OPTIONS
	private String ntpServer2;
	// @OPTIONS
	private String ntpServer3;

	public NTPCfg() {
		setType(ConfigDataType.NTPCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("server1")
	public String getNtpServer1() {
		return ntpServer1;
	}

	public void setNtpServer1(String ntpServer1) {
		this.ntpServer1 = ntpServer1;
	}

	@JsonProperty("server2")
	public String getNtpServer2() {
		return ntpServer2;
	}

	public void setNtpServer2(String ntpServer2) {
		this.ntpServer2 = ntpServer2;
	}

	@JsonProperty("server3")
	public String getNtpServer3() {
		return ntpServer3;
	}

	public void setNtpServer3(String ntpServer3) {
		this.ntpServer3 = ntpServer3;
	}

	@JsonProperty("time_zone")
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		return "NTPTimeData [ntpServer1=" + ntpServer1 + ", ntpServer2="
				+ ntpServer2 + ", ntpServer3=" + ntpServer3 + ", timeZone="
				+ timeZone + "]";
	}
}
