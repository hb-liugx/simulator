package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class LogFilter extends PeaxyBaseDomain {
	private String includes;

	//private LogLevel level;

	private boolean syslogEnable;

	@JsonProperty("includes")
	public String getIncludes() {
		return includes;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

	//@JsonProperty("level")
	//public LogLevel getLevel() {
	//	return level;
	//}

	//public void setLevel(LogLevel logLevel) {
	//	this.level = logLevel;
	//}

	@JsonProperty("enable_syslog")
	public boolean getSyslogEnable() {
		return syslogEnable;
	}

	public void setSyslogEnable(boolean syslogEnable) {
		this.syslogEnable = syslogEnable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((includes == null) ? 0 : includes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogFilter other = (LogFilter) obj;
		if (includes == null) {
			if (other.includes != null)
				return false;
		} else if (!includes.equals(other.includes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogFilter [includes=").append(includes)
				//.append(", level=").append(level)
				.append(", syslogEnable=")
				.append(syslogEnable).append("]");
		return builder.toString();
	}
}
