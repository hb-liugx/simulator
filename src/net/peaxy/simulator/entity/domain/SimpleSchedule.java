package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Simple ad-hoc schedule
 */
@XmlType(name = "Schedule")
@JsonTypeName(SimpleSchedule.TYPE)
public class SimpleSchedule extends PeaxyBaseDomain implements Schedule {

	public static final String TYPE = "simple";

	private Long start;

	private Integer interval;

	/**
	 * The start time of the schedule. This is the ms since the epoch.
	 * 
	 * @return
	 */
	@JsonProperty("start_time")
	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * The repeating interval in minutes
	 * 
	 * @return
	 */
	@JsonProperty("interval_minutes")
	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}
}
