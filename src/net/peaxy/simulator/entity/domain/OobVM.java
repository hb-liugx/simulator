package net.peaxy.simulator.entity.domain;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class OobVM extends PeaxyBaseDomain {
    
    private String ip;
    
    private long id;
    
    private String name;
    
    private long host_id;
    
    private NodeType type;
    
    private int capacity;
    
    private DSPerfType drive_type;
    
    private String dataPartition;
    
    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }
    
    @JsonIgnore
    public InetAddress getAddress() {
        try {
            return InetAddress.getByName(ip);
        }
        catch ( UnknownHostException uex ) {
            throw new RuntimeException("this is not happening, bad ip string[" + ip + "]");
        }        
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("host_id")
    public long getHostId() {
        return host_id;
    }

    public void setHostId(long hostId) {
        this.host_id = hostId;
    }

    @JsonProperty("type")
    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }
    
    public void sanityCheck() throws Exception {

    }

    @JsonProperty("capacity")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @JsonProperty("drive_type")
    public DSPerfType getDriveType() {
        return drive_type;
    }

    public void setDriveType(DSPerfType driveType) {
        this.drive_type = driveType;
    }

    @JsonProperty("data")
    public String getDataPartition() {
        return dataPartition;
    }

    public void setDataPartition(String dataPartition) {
        this.dataPartition = dataPartition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        OobVM other = (OobVM) obj;
        if ( id != other.id )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OobVM [ip=" + ip + ", id=" + id + ", hostId=" + host_id + ", type=" + type + "]";
    }
    
}
