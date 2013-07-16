package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class VmConfig extends PeaxyBaseDomain {

	private int id;
    private String name;
    private String os_slot;
    private String data_slot;
    private int capacity;
    private DSPerfType drive_type;
    private VmType type;
    private VmState state;
    private VmKind kind;
    private int cores;
    private int memory;
    private String ip;
    
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
    
    @JsonProperty("os_slot")
    public String getOs() {
        return os_slot;
    }
    
    public void setOs(String slot) {
        this.os_slot = slot;
    }
    
    @JsonProperty("data_slot")
    public String getData() {
        return data_slot;
    }
    
    public void setData(String data) {
        this.data_slot = data;
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

    @JsonProperty("type")
    public VmType getType() {
        return type;
    }
    
    public void setType(VmType type) {
        this.type = type;
    }
    
    @JsonProperty("state")
    public VmState getState() {
        return state;
    }
    
    public void setState(VmState state) {
        this.state = state;
    }
    
    @JsonProperty("kind")
    public VmKind getKind() {
        return kind;
    }
    
    public void setKind(VmKind kind) {
        this.kind = kind;
    }
    
    @JsonProperty("cores")
    public int getCores() {
        return cores;
    }
    
    public void setCores(int cores) {
        this.cores = cores;
    }
    
    @JsonProperty("memory")
    public int getMemory() {
        return memory;
    }
    
    public void setMemory(int memory) {
        this.memory = memory;
    }

    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VmConfig [id=").append(id).append(", name=")
                .append(name).append(", os=").append(os_slot).append(", data=")
                .append(data_slot).append(", capacity=").append(capacity)
                .append(", driveType=").append(drive_type).append(", type=")
                .append(type).append(", state=").append(state)
                .append(", kind=").append(kind).append(", cores=")
                .append(cores).append(", memory=").append(memory)
                .append(", ip=").append(ip).append("]");
        return builder.toString();
    }

}
