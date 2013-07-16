package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class VmInfo extends PeaxyBaseDomain {
	private long id;
	private String name;
	private String os;
	private String data;
	private long capacity;
	private DSPerfType driveType;
	private VmType type;
	private VmState state;
	private VmKind kind;
	private int cores;
	private long memory;
	private String ip;

	@JsonProperty("id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("os")
	public String getOs() {
		return os;
	}

	public void setOs(String slot) {
		this.os = slot;
	}

	@JsonProperty("data")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@JsonProperty("capacity")
	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	@JsonProperty("drive_type")
	public DSPerfType getDriveType() {
		return driveType;
	}

	public void setDriveType(DSPerfType driveType) {
		this.driveType = driveType;
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
	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
