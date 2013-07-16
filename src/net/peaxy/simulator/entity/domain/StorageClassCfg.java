package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class StorageClassCfg extends PeaxyBaseDomain {
	
	private String name;
    
    private String description;
    
    private int replication_factor;
    
    // capacity in giga-byte
    private int capacity_gb;

    // storage class performance type based drive access speed
    private DSPerfType device_type;
        
    private boolean default_sc;
    
    public void validate() {

    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("replication_factor")
    public int getCardinality() {
        return replication_factor;
    }

    public void setCardinality(int cardinality) {
        this.replication_factor = cardinality;
    }

    // capacity in giga-byte
    @JsonProperty("capacity_gb")
    public int getCapacityGb() {
        return capacity_gb;
    }

    public void setCapacityGb(int capacityGb) {
        this.capacity_gb = capacityGb;
    }

    @JsonProperty("device_type")
    public DSPerfType getType() {
        return device_type;
    }

    public void setType(DSPerfType type) {
        this.device_type = type;
    }

    @JsonProperty("default_sc")
    public boolean isDefaultSC() {
        return default_sc;
    }

    public void setDefaultSC(boolean defaultSC) {
        this.default_sc = defaultSC;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StorageClassCfg [name=").append(name)
                .append(", description=").append(description)
                .append(", cardinality=").append(replication_factor)
                .append(", capacityGb=").append(capacity_gb).append(", type=")
                .append(device_type).append(", defaultSC=").append(default_sc)
                .append("]");
        return builder.toString();
    }
}
