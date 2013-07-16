package net.peaxy.simulator.entity.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

public class LogCfg extends ConfigData {
	private static Logger logger = Logger.getLogger(LogCfg.class);
	public static final int SCHEMA_VERSION = 1;

	private String server;

	private int port;

	// This is a list of log level list based on different includes
	private List<LogFilter> filterList;

	public LogCfg() {
		setType(ConfigDataType.LogCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("log_server")
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@JsonProperty("port")
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@JsonProperty("filter_list")
	public List<LogFilter> getFilterList() {
		return filterList;
	}

	public void add(LogFilter filter) {
		if (filterList == null) {
			filterList = new ArrayList<LogFilter>();
		}

		if (!filterList.contains(filter)) {
			filterList.add(filter);
		}
	}

	public void setFilterList(List<LogFilter> filterList) {
		this.filterList = filterList;
	}

	public LogFilter getLogFilterByHs(Integer hsId) {
		LogFilter defaultLevel = null;

		for (LogFilter loglevel : getFilterList()) {
			if (loglevel.getIncludes().equals(SMConstants.DEFAULT_ALL_HS)) {
				defaultLevel = loglevel;
			} else {
				String inc = loglevel.getIncludes();
				String[] sp = inc.trim().split(",");
				try {
					for (String s : sp) {
						// Check for range
						if (s.contains("-")) {
							String[] range = s.split("-");
							if (hsId.intValue() >= Integer.parseInt(range[0]
									.trim())
									&& hsId.intValue() <= Integer
											.parseInt(range[1].trim())) {
								return loglevel;
							}
						} else if (s.trim().equals(hsId.toString())) {
							return loglevel;
						}
					}
				} catch (Exception ex) {
					logger.error("getLogFilterByHs: Invalid log filter " + inc,
							ex);
				}
			}
		}

		if (defaultLevel == null) {
			logger.debug("Using default WARNING level");
			defaultLevel = new LogFilter();
			//defaultLevel.setLevel(LogLevel.WARNING);
		}

		return defaultLevel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogCfg [server=").append(server).append(", port=")
				.append(port).append(", filterList=").append(filterList)
				.append("]");
		return builder.toString();
	}
}
