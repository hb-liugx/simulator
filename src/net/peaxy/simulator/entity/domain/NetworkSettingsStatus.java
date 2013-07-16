package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

/*
 * File:   NetworkSettingsStatus.java
 * Author: Peter Steele
 *
 * Unpublished source code.
 *
 * Copyright (c) 2013, Peaxy, Inc. <engineering@peaxy.net>
 *
 * All rights reserved.
 *
 */
/**
 * 
 * @author root
 * 
 */

public class NetworkSettingsStatus extends PeaxyBaseDomain {
	/**
     * True if the specified hyperfiler name is valid. It can contain letters,
     * numbers, hyphens and underscores, and it must be no more than 14 characters
     * in length.
     */
    private boolean hyperfiler_name_valid;
	/**
	 * Specifies the status of the management IP. To be considered valid, this
	 * field should be NOT_PINGABLE.
	 */
	private IpStatus management_ip_status;
	/**
	 * Specifies the status of the gateway. To be considered valid, this field
	 * should be PINGABLE.
	 */
	private IpStatus gateway_status;
	/**
	 * True if the specified subnet mask is a syntactically correct mask. The
	 * subnet mask could potentially be validated "live" directly in the UI as
	 * it is entered so the additional validation here may not be needed.
	 */
	private boolean netmask_valid;
	/**
	 * True if the specified domain is a syntactically correct domain name. The
	 * domain name could potentially be validated "live" directly in the UI as
	 * it is entered so the additional validation here may not be needed.
	 */
	private boolean dns_domain_valid;
	/**
	 * These next three fields specify the status of supplied domain servers.
	 * Only one domain server is required, but up to three can be defined. To be
	 * considered valid, one of the three DNS server fields must be PINGABLE.
	 */
	private IpStatus dns_server1_status;
	private IpStatus dns_server2_status;
	private IpStatus dns_server3_status;
	/**
	 * These next three fields specify the status of supplied NTP servers. Only
	 * one NTP server is required, but up to three can be defined. To be
	 * considered valid, one of the three NTP server fields must be PINGABLE.
	 */
	private IpStatus ntp_server1_status;
	private IpStatus ntp_server2_status;
	private IpStatus ntp_server3_status;

	@JsonProperty("hyperfiler_name_valid")
    public boolean isHyperfilerNameValid() {
        return hyperfiler_name_valid;
    }

    public void setHyperfilerNameValid(boolean hyperfilerNameValid) {
        this.hyperfiler_name_valid = hyperfilerNameValid;
    }
    
	@JsonProperty("management_ip_status")
	public IpStatus getManagementIpStatus() {
		return management_ip_status;
	}

	public void setManagementIpStatus(IpStatus managementIpStatus) {
		this.management_ip_status = managementIpStatus;
	}

	@JsonProperty("gateway_status")
	public IpStatus getGatewayStatus() {
		return gateway_status;
	}

	public void setGatewayStatus(IpStatus gatewayStatus) {
		this.gateway_status = gatewayStatus;
	}

	@JsonProperty("netmask_valid")
	public boolean isNetmaskValid() {
		return netmask_valid;
	}

	public void setNetmaskValid(boolean netmaskValid) {
		this.netmask_valid = netmaskValid;
	}

	@JsonProperty("dns_domain_valid")
	public boolean isDnsDomainValid() {
		return dns_domain_valid;
	}

	public void setDnsDomainValid(boolean dnsDomainValid) {
		this.dns_domain_valid = dnsDomainValid;
	}

	@JsonProperty("dns_server1_status")
	public IpStatus getDnsServer1Status() {
		return dns_server1_status;
	}

	public void setDnsServer1Status(IpStatus dnsServer1Status) {
		this.dns_server1_status = dnsServer1Status;
	}

	@JsonProperty("dns_server2_status")
	public IpStatus getDnsServer2Status() {
		return dns_server2_status;
	}

	public void setDnsServer2Status(IpStatus dnsServer2Status) {
		this.dns_server2_status = dnsServer2Status;
	}

	@JsonProperty("dns_server3_status")
	public IpStatus getDnsServer3Status() {
		return dns_server3_status;
	}

	public void setDnsServer3Status(IpStatus dnsServer3Status) {
		this.dns_server3_status = dnsServer3Status;
	}

	@JsonProperty("ntp_server1_status")
	public IpStatus getNtpServer1Status() {
		return ntp_server1_status;
	}

	public void setNtpServer1Status(IpStatus ntpServer1Status) {
		this.ntp_server1_status = ntpServer1Status;
	}

	@JsonProperty("ntp_server2_status")
	public IpStatus getNtpServer2Status() {
		return ntp_server2_status;
	}

	public void setNtpServer2Status(IpStatus ntpServer2Status) {
		this.ntp_server2_status = ntpServer2Status;
	}

	@JsonProperty("ntp_server3_status")
	public IpStatus getNtpServer3Status() {
		return ntp_server3_status;
	}

	public void setNtpServer3Status(IpStatus ntpServer3Status) {
		this.ntp_server3_status = ntpServer3Status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetworkSettingsValidation [managementIpStatus=")
				.append(management_ip_status).append(", gatewayStatus=")
				.append(gateway_status).append(", netmaskValid=")
				.append(netmask_valid).append(", dnsDomainValid=")
				.append(dns_domain_valid).append(", dnsServer1Status=")
				.append(dns_server1_status).append(", dnsServer2Status=")
				.append(dns_server2_status).append(", dnsServer3Status=")
				.append(dns_server3_status).append(", ntpServer1Status=")
				.append(ntp_server1_status).append(", ntpServer2Status=")
				.append(ntp_server2_status).append(", ntpServer3Status=")
				.append(ntp_server3_status).append("]");
		return builder.toString();
	}
}
