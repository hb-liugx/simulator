package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class HvInfo extends PeaxyBaseDomain {
	private int id;
	private String hostname;
	private String ip;
	private HvType type;
	private List<VmConfig> vm_list;

	private int status;
	private int process;

	@JsonProperty("id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty("hostname")
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@JsonProperty("type")
	public HvType getType() {
		return type;
	}

	public void setType(HvType type) {
		this.type = type;
	}

	@JsonProperty("vm_list")
	public List<VmConfig> getVmList() {
		return vm_list;
	}

	public void setVmList(List<VmConfig> vmList) {
		this.vm_list = vmList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

}
