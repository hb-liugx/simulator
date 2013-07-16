package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class Condition extends PeaxyBaseDomain {
	private String dir;

	private String extension;

	private Integer unModifiedMinutes;

	private Integer unAccessedMinutes;
	
	private Integer currentSC;
	/**
	 * The directory of a file
	 * 
	 * @return
	 */
	@JsonProperty("directory")
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * The extention of a file.
	 * 
	 * @return
	 */
	@JsonProperty("extention")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * The number of minutes a file has not been modified
	 * 
	 * @return
	 */
	@JsonProperty("not_modified_minutes")
	public Integer getUnModifiedMinutes() {
		return unModifiedMinutes;
	}

	public void setUnModifiedMinutes(Integer unModifiedMinutes) {
		this.unModifiedMinutes = unModifiedMinutes;
	}

	/**
	 * TRhe number of minutes a file has not been accessed
	 * 
	 * @return
	 */
	@JsonProperty("not_access_minutes")
	public Integer getUnAccessedMinutes() {
		return unAccessedMinutes;
	}

	public void setUnAccessedMinutes(Integer unAccessedMinutes) {
		this.unAccessedMinutes = unAccessedMinutes;
	}
	
	@JsonProperty("currentSC")
	public Integer getCurrentSC() {
		return currentSC;
	}

	public void setCurrentSC(Integer currentSC) {
		this.currentSC = currentSC;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Condition [dir=");
		builder.append(dir);
		builder.append(", extention=");
		builder.append(extension);
		builder.append(", unModifiedMinutes=");
		builder.append(unModifiedMinutes);
		builder.append(", unAccessedMinutes=");
		builder.append(unAccessedMinutes);
		builder.append(", currentSC=");
		builder.append(currentSC);
		builder.append("]");
		return builder.toString();
	}
}
