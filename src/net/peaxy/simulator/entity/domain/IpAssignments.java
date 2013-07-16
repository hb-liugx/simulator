package net.peaxy.simulator.entity.domain;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class IpAssignments extends PeaxyBaseDomain {
	/**
	 * A map of the IP address assigned to each server.
	 */
	private Map<Long, String> ip_list;
	/**
	 * If the IpInfo object did not specify enough IP addresses in the setIpInfo
	 * call, this field is set to the number of IP addresses still needed by the
	 * servers and the eventual set of VMs that will be created for them. Note
	 * that if this field is greater than zero, no IP assignments are defined in
	 * the ipList field. This implies that the user needs to provide additional
	 * IP addresses.
	 */
	private int additional_needed;
	/**
	 * Indicates which IP addresses in the range of addresses provided are
	 * already in use. This isn't necessarily an error, but each in-use IP
	 * address subtracts from the range of addresses that was given, potentially
	 * reducing the count below what is required. If additionalNeeded > 0, this
	 * means another call to setIpInfo will be needed with additional IP
	 * addresses defined. The UI can choose to display a message potentially
	 * including the addresses already in use.
	 */
	private List<String> in_use;
	/**
	 * List of IP addresses that are available for use that were generated from
	 * the IP ranges specified in the call to setIpInfo call.
	 */
	private List<String> available;

	@JsonProperty("ip_list")
	public Map<Long, String> getIpList() {
		return ip_list;
	}

	public void setIpList(Map<Long, String> ipList) {
		this.ip_list = ipList;
	}

	@JsonProperty("additional_needed")
	public int getAdditionalNeeded() {
		return additional_needed;
	}

	public void setAdditionalNeeded(int additionalNeeded) {
		this.additional_needed = additionalNeeded;
	}

	@JsonProperty("in_use")
	public List<String> getInUse() {
		return in_use;
	}

	public void setInUse(List<String> inUse) {
		this.in_use = inUse;
	}

	@JsonProperty("available")
	public List<String> getAvailable() {
		return available;
	}

	public void setAvailable(List<String> available) {
		this.available = available;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IpAssignments [ipList=").append(ip_list)
				.append(", additionalNeeded=").append(additional_needed)
				.append(", inUse=").append(in_use).append(", available=")
				.append(available).append("]");
		return builder.toString();
	}

}
