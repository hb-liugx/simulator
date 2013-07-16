package net.peaxy.simulator.entity.domain;

import java.util.List;

public class NodeConfig extends ConfigData {
	private long id;
    private String hostname;
    private String ipAddress;
    private String netmask;
    private String gateway;
    private List<String> bondList;
    
    public NodeConfig() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }
    
    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public List<String> getBondList() {
        return bondList;
    }

    public void setBondList(List<String> bondList) {
        this.bondList = bondList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeConfig [id=").append(id).append(", hostname=")
                .append(hostname).append(", ipAddress=").append(ipAddress)
                .append(", netmask=").append(netmask).append(", gateway=")
                .append(gateway).append(", bondList=").append(bondList)
                .append("]");
        return builder.toString();
    }
}
