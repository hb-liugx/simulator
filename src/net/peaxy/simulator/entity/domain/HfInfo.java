package net.peaxy.simulator.entity.domain;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class HfInfo extends PeaxyBaseDomain {
    private int leader_id;
    private int total_capacity;
    private Map<Integer, HvInfo> hv_info;
    
    @JsonProperty("leader_id")
    public void setLeaderId(int leaderId) {
        this.leader_id = leaderId;
    }

    public int getLeaderId() {
        return leader_id;
    }

    @JsonProperty("total_capacity")
    public int getTotalCapacity() {
        return total_capacity;
    }
    
    public void setTotalCapacity(int totalCapacity) {
        this.total_capacity = totalCapacity;
    }
    
    @JsonProperty("hv_info")
    public Map<Integer, HvInfo> getHvInfoMap() {
        return hv_info;
    }
    
    public void setHvInfoMap(Map<Integer, HvInfo> hvInfoMap) {
        this.hv_info = hvInfoMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HfInfo [totalCapacity=").append(total_capacity)
                .append(", hvInfoMap=").append(hv_info).append("]");
        return builder.toString();
    }
}
