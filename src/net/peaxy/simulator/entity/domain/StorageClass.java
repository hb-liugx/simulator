package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StorageClass extends PeaxyBaseDomain {
	private int id;
    
    private String name;
    
    private String description;
    
    private List<Integer> dsList;
    
    private int cardinality;
    
    // capacity in giga-byte
    private int capacityGb;
    
    private DSPerfType type;
    
    private boolean defaultSC = false;
    
    public StorageClass() {}

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ds_list")
    public List<Integer> getDsList() {
        return dsList;
    }

    public void setDsList(List<Integer> dsList) {
        this.dsList = dsList;
    }

    @JsonProperty("replicas")
    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("capacity_gb")
    public int getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(int capacityGb) {
        this.capacityGb = capacityGb;
    }

    @JsonProperty("device_type")
    public DSPerfType getType() {
        return type;
    }

    public void setType(DSPerfType type) {
        this.type = type;
    }

    @JsonProperty("default_sc")
    public boolean isDefaultSC() {
        return defaultSC;
    }

    public void setDefaultSC(boolean defaultSC) {
        this.defaultSC = defaultSC;
    }
}
