package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SystemConfig extends PeaxyBaseDomain {
	public static final int CARDINALITY = 3;
    public static final int SPARE = 0;
    public static final int LOG_SERVER_PORT = 514;
        
    private String cluster_name;
    private String management_ip;
    private int cluster_id;
    // Net
    private String gate_way;
	private String subnet_mask;
    private String dns_domain;
   
    //Time
    private String ntp_server1;
    private String ntp_server2;
    private String ntp_server3;
    private String time_zone;
    
    // DNS
    private String dns_search1;
    private String dns_search2;
    private String dns_search3;
    private String dns_server1;
    private String dns_server2;
    private String dns_server3;
    
    // Email setting
    private String email_protocol;
    private String email_server;
    private int email_port;
    private String email_login;
    private String email_password;
    private String email_from;
    
    //AAA
    private String user_name;
    private String password;
    private String user_email;    
    
    //Misc setting
    private int cardinality = CARDINALITY;
    private int spare_count = SPARE;
    private boolean replacement_enabled = true;
    private boolean syslogEnabled = false;
    
    private String stat_server_ip;
    private int stat_server_port;
    
    private String log_server_ip;
    private int log_server_port = LOG_SERVER_PORT;
    
    private List<StorageClassCfg> storage_classes;
    
    private List<RuleCfg> ingest_policy;
    
    public SystemConfig() {}
    
    @JsonProperty("cluster_name")
    public String getClusterName() {
        return cluster_name;
    }

    public void setClusterName(String clusterName) {
        this.cluster_name = clusterName;
    }

    @JsonProperty("cluster_id")    
    public int getClusterId() {
        return cluster_id;
    }

    public void setClusterId(int clusterId) {
        this.cluster_id = clusterId;
    }

    @JsonProperty("management_ip")
    public String getManagementIp() {
        return management_ip;
    }
    
    public void setManagementIp(String managementIp) {
        this.management_ip = managementIp;
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
    
    @JsonProperty("time_zone")    
    public String getTimeZone() {
        return time_zone;
    }
    
    public void setTimeZone(String timeZone) {
        this.time_zone = timeZone;
    }

    @JsonProperty("dns_search1")    
    public String getDnsSearch1() {
        return dns_search1;
    }

    public void setDnsSearch1(String dnsSearch1) {
        this.dns_search1 = dnsSearch1;
    }

    @JsonProperty("dns_search2")    
    public String getDnsSearch2() {
        return dns_search2;
    }

    public void setDnsSearch2(String dnsSearch2) {
        this.dns_search2 = dnsSearch2;
    }

    @JsonProperty("dns_search3")    
    public String getDnsSearch3() {
        return dns_search3;
    }

    public void setDnsSearch3(String dnsSearch3) {
        this.dns_search3 = dnsSearch3;
    }

    @JsonProperty("dns_server1")    
    public String getDnsServer1() {
        return dns_server1;
    }

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

    @JsonProperty("email_protocol")    
    public String getEmailProtocol() {
        return email_protocol;
    }
    
    public void setEmailProtocol(String emailProtocol) {
        this.email_protocol = emailProtocol;
    }

    @JsonProperty("email_server")    
    public String getEmailServer() {
        return email_server;
    }
    
    public void setEmailServer(String emailServer) {
        this.email_server = emailServer;
    }
    
    @JsonProperty("email_port")    
    public int getEmailPort() {
        return email_port;
    }
    
    public void setEmailPort(int emailPort) {
        this.email_port = emailPort;
    }
    
    @JsonProperty("email_login")    
    public String getEmailLogin() {
        return email_login;
    }
    
    public void setEmailLogin(String emailLogin) {
        this.email_login = emailLogin;
        user_email = emailLogin;
    }

    @JsonProperty("email_password")    
    public String getEmailPassword() {
        return email_password;
    }
    
    public void setEmailPassword(String emailPassword) {
        this.email_password = emailPassword;
    }
    
    @JsonProperty("email_from")    
    public String getEmailFrom() {
        return email_from;
    }
    
    public void setEmailFrom(String emailFrom) {
        this.email_from = emailFrom;
    }
    
    @JsonProperty("user_name")    
    public String getUserName() {
        return user_name;
    }
    
    public void setUserName(String userName) {
        this.user_name = userName;
    }
    
    @JsonProperty("password")    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @JsonProperty("user_email")
    public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(String userEmail) {
        this.user_email = userEmail;
    }

    @JsonProperty("cardinality")    
    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    @JsonProperty("spare_count")    
    public int getSpareCount() {
        return spare_count;
    }

    public void setSpareCount(int spareCount) {
        this.spare_count = spareCount;
    }

    @JsonProperty("replacement_enabled")
    public boolean isMemberReplacementEnabled() {
        return replacement_enabled;
    }

    public void setMemberReplacementEnabled(boolean memberReplacementEnabled) {
        this.replacement_enabled = memberReplacementEnabled;
    }

    public boolean isSyslogEnabled() {
        return syslogEnabled;
    }

    public void setSyslogEnabled(boolean syslogEnabled) {
        this.syslogEnabled = syslogEnabled;
    }

    @JsonProperty("stat_server_ip")
    public String getStatServerIp() {
        return stat_server_ip;
    }

    public void setStatServerIp(String statServerIp) {
        this.stat_server_ip = statServerIp;
    }

    @JsonProperty("stat_server_port")
    public int getStatServerPort() {
        return stat_server_port;
    }

    public void setStatServerPort(int statServerPort) {
        this.stat_server_port = statServerPort;
    }

    @JsonProperty("log_server_ip")
    public String getLogServerIp() {
        return log_server_ip;
    }

    public void setLogServerIp(String logServerIp) {
        this.log_server_ip = logServerIp;
    }

    @JsonProperty("log_server_port")
    public int getLogServerPort() {
        return log_server_port;
    }

    public void setLogServerPort(int logServerPort) {
        this.log_server_port = logServerPort;
    }

    @JsonProperty("storage_classes")
    public List<StorageClassCfg> getScList() {
        return storage_classes;
    }

    public void setScList(List<StorageClassCfg> scList) {
        this.storage_classes = scList;
    }

    @JsonProperty("ingest_policy")
    public List<RuleCfg> getInjestList() {
        return ingest_policy;
    }

    public void setInjestList(List<RuleCfg> injestList) {
        this.ingest_policy = injestList;
    }
    
    @JsonProperty("gate_way")
    public String getGateWay() {
		return gate_way;
	}

	public void setGateWay(String gateWay) {
		this.gate_way = gateWay;
	}

	@JsonProperty("subnet_mask")
	public String getSubnetMask() {
		return subnet_mask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnet_mask = subnetMask;
	}

	@JsonProperty("dns_domain")
	public String getDnsDomain() {
		return dns_domain;
	}

	public void setDnsDomain(String dnsDomain) {
		this.dns_domain = dnsDomain;
	}
    public void validate() {
        if ( storage_classes == null || storage_classes.isEmpty() )
            throw new IllegalArgumentException("no storage class defined");
        if ( user_name == null || password == null || user_email == null )
            throw new IllegalArgumentException("admin user information incomplete");
        
        for ( StorageClassCfg sc : storage_classes )
            sc.validate();
    }
    
    public ConfigData buildConfigData(ConfigDataType type){
    	
    	return null;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemConfig [clusterName=").append(cluster_name)
                .append(", managementIp=").append(management_ip)
                .append(", clusterId=").append(cluster_id)
                .append(", ntpServer1=").append(ntp_server1)
                .append(", ntpServer2=").append(ntp_server2)
                .append(", ntpServer3=").append(ntp_server3)
                .append(", timeZone=").append(time_zone).append(", dnsSearch1=")
                .append(dns_search1).append(", dnsSearch2=").append(dns_search2)
                .append(", dnsSearch3=").append(dns_search3)
                .append(", dnsServer1=").append(dns_server1)
                .append(", dnsServer2=").append(dns_server2)
                .append(", dnsServer3=").append(dns_server3)
                .append(", emailProtocol=").append(email_protocol)
                .append(", emailServer=").append(email_server)
                .append(", emailPort=").append(email_port)
                .append(", emailLogin=").append(email_login)
                .append(", emailPassword=").append(email_password)
                .append(", emailFrom=").append(email_from).append(", userName=")
                .append(user_name).append(", password=").append(password)
                .append(", userEmail=").append(user_email)
                .append(", cardinality=").append(cardinality)
                .append(", spareCount=").append(spare_count)
                .append(", memberReplacementEnabled=")
                .append(replacement_enabled).append(", syslogEnabled=")
                .append(syslogEnabled).append(", statServerIp=")
                .append(stat_server_ip).append(", statServerPort=")
                .append(stat_server_port).append(", logServerIp=")
                .append(log_server_ip).append(", logServerPort=")
                .append(log_server_port).append(", scList=").append(storage_classes)
                .append(", ingestList=").append(ingest_policy).append("]");
        return builder.toString();
    }
}
