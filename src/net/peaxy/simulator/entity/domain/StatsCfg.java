package net.peaxy.simulator.entity.domain;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StatsCfg extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	private String statsServerIp;

	/* This can be set to default value */
	private int statsServerPort;

	/* Stats collection interval */
	// Need to find out if different interval required for diff stats type
	private int interval;

	// This is a list of stats that are enabled
	List<StatsDataType> enabledStats;

	public StatsCfg() {
		setType(ConfigDataType.StatsCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("server_ip")
	public String getStatsServerIp() {
		return statsServerIp;
	}

	public void setStatsServerIp(String statsServerIp) {
		this.statsServerIp = statsServerIp;
	}

	@JsonProperty("server_port")
	public int getStatsServerPort() {
		return statsServerPort;
	}

	public void setStatsServerPort(int statsServerPort) {
		this.statsServerPort = statsServerPort;
	}

	@JsonProperty("interval")
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@JsonProperty("enabled_stats")
	public List<StatsDataType> getEnabledStats() {
		return enabledStats;
	}

	public void enable(StatsDataType type) {
		if (enabledStats == null) {
			enabledStats = new ArrayList<StatsDataType>();
		}

		if (!enabledStats.contains(type)) {
			enabledStats.add(type);
		}
	}

	public void setEnabledStats(List<StatsDataType> enabledStats) {
		this.enabledStats = enabledStats;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatsCfg [statsServerIp=").append(statsServerIp)
				.append(", statsServerPort=").append(statsServerPort)
				.append(", interval=").append(interval)
				.append(", enabledStats=").append(enabledStats).append("]");
		return builder.toString();
	}
}
