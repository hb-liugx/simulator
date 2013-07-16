package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class StorageClassData extends PeaxyBaseDomain {
	private int id;
	private int totalMB;
	private int usedMB;
	private int freeMB;

	/**
	 * Internal tracking ID
	 * 
	 * @return
	 */
	@JsonProperty("id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Total capacity of the storage class in MB
	 * 
	 * @return
	 */
	@JsonProperty("total_mb")
	public int getTotalMB() {
		return totalMB;
	}

	public void setTotalMB(int totalMB) {
		this.totalMB = totalMB;
	}

	/**
	 * Used capacity of the storage class in MB
	 * 
	 * @return
	 */
	@JsonProperty("used_mb")
	public int getUsedMB() {
		return usedMB;
	}

	public void setUsedMB(int usedMB) {
		this.usedMB = usedMB;
	}

	/**
	 * Available capacity in storage class
	 * 
	 * @return
	 */
	@JsonProperty("free_mb")
	public int getFreeMB() {
		return freeMB;
	}

	public void setFreeMB(int freeMB) {
		this.freeMB = freeMB;
	}
}
