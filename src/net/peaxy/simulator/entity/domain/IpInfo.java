package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class IpInfo extends PeaxyBaseDomain {
	/**
	 * The subset of servers selected from the list that were discovered.
	 */
	List<Long> id_list;
	/**
	 * A list of single IPs and IP ranges representing a set of IP addresses. An
	 * IP range can be of the form X.Y.Z.lo-hi or X.Y.Z.*. The '*' notation is
	 * equivalent to a range of 1-254. Example:
	 * 172.16.99.1,172.16.99.5-20,172.16.100.*
	 */
	List<String> ip_range_list;

	@JsonProperty("id_list")
	public List<Long> getIdList() {
		return id_list;
	}

	public void setIdList(List<Long> idList) {
		this.id_list = idList;
	}

	@JsonProperty("ip_range_list")
	public List<String> getIpRangeList() {
		return ip_range_list;
	}

	public void setIpRangeList(List<String> ipRangeList) {
		this.ip_range_list = ipRangeList;
	}

}
