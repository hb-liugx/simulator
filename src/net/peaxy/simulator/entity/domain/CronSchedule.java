package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

@XmlType(name = "Schedule")
@JsonTypeName(CronSchedule.TYPE)
public class CronSchedule extends PeaxyBaseDomain implements Schedule {

	public static final String TYPE = "cron";

	private String expresssion;

	@JsonProperty("expression")
	public String getExpresssion() {
		return expresssion;
	}

	public void setExpresssion(String expresssion) {
		this.expresssion = expresssion;
	}
}
