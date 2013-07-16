package net.peaxy.simulator.entity.domain;

import com.owlike.genson.annotation.JsonProperty;

/*
 * File:   NetworkSettings.java
 * Author: Peter Steele
 *
 * Unpublished source code.
 *
 * Copyright (c) 2013, Peaxy, Inc. <engineering@peaxy.net>
 *
 * All rights reserved.
 *
 */

public class NetworkSettings extends PeaxyBaseDomain {
	private String hyperfiler_name;
    private String management_ip;
    private String gateway;
    private String netmask;
    private String dns_domain;
    private String dns_server1;
    private String dns_server2;
    private String dns_server3;
    private String ntp_server1;
    private String ntp_server2;
    private String ntp_server3;

    @JsonProperty("hyperfiler_name")
    public String getHyperfilerName() {
        return hyperfiler_name;
    }

    public void setHyperfilerName(String hyperfilerName) {
        this.hyperfiler_name = hyperfilerName;
    }

    @JsonProperty("management_ip")
    public String getManagementIp() {
        return management_ip;
    }
    
    public void setManagementIp(String managementIp) {
        this.management_ip = managementIp;
    }
    
    @JsonProperty("gateway")
    public String getGateway() {
        return gateway;
    }
    
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    
    @JsonProperty("netmask")
    public String getNetmask() {
        return netmask;
    }
    
    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }
    
    @JsonProperty("dns_domain")
    public String getDnsDomain() {
        return dns_domain;
    }
    
    public void setDnsDomain(String dnsDomain) {
        this.dns_domain = dnsDomain;
    }
    
    @JsonProperty("dns_server1")
    public String getDnsServer1() {
        return dns_server1;
    }
    
    @JsonProperty("dns_server1")
    public void setDnsServer1(String dnsServer1) {
        this.dns_server1 = dnsServer1;
    }
    
    @JsonProperty("dns_server2")
    public String getDnsServer2() {
        return dns_server2;
    }
    
    public void setDnsServer2(String dnsServer2) {
        this.dns_server2 = dnsServer2;
    }
    
    @JsonProperty("dns_server3")
    public String getDnsServer3() {
        return dns_server3;
    }
    
    public void setDnsServer3(String dnsServer3) {
        this.dns_server3 = dnsServer3;
    }
    
    @JsonProperty("ntp_server1")
    public String getNtpServer1() {
        return ntp_server1;
    }
    
    public void setNtpServer1(String ntpServer1) {
        this.ntp_server1 = ntpServer1;
    }
    
    @JsonProperty("ntp_server2")
    public String getNtpServer2() {
        return ntp_server2;
    }
    
    public void setNtpServer2(String ntpServer2) {
        this.ntp_server2 = ntpServer2;
    }
    
    @JsonProperty("ntp_server3")
    public String getNtpServer3() {
        return ntp_server3;
    }
    
    public void setNtpServer3(String ntpServer3) {
        this.ntp_server3 = ntpServer3;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetworkSettings [managementIp=").append(management_ip)
				.append(", gateway=").append(gateway).append(", netmask=")
				.append(netmask).append(", dnsDomain=").append(dns_domain)
				.append(", dnsServer1=").append(dns_server1)
				.append(", dnsServer2=").append(dns_server2)
				.append(", dnsServer3=").append(dns_server3)
				.append(", ntpServer1=").append(ntp_server1)
				.append(", ntpServer2=").append(ntp_server2)
				.append(", ntpServer3=").append(ntp_server3).append("]");
		return builder.toString();
	}
}
