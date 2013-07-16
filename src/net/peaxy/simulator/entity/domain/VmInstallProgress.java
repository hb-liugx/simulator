package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class VmInstallProgress extends PeaxyBaseDomain {
	private int percent_complete;
	private int installs_pending;
	private int installs_started;
	private int installs_completed;
	private int installs_failed;
	private List<String> failed_list;

	@JsonProperty("percent_complete")
	public int getPercentComplete() {
		return percent_complete;
	}

	public void setPercentComplete(int percentComplete) {
		this.percent_complete = percentComplete;
	}

	@JsonProperty("installs_pending")
	public int getInstallsPending() {
		return installs_pending;
	}

	public void setInstallsPending(int installsPending) {
		this.installs_pending = installsPending;
	}

	@JsonProperty("installs_started")
	public int getInstallsStarted() {
		return installs_started;
	}

	public void setInstallsStarted(int installsStarted) {
		this.installs_started = installsStarted;
	}

	@JsonProperty("installs_completed")
	public int getInstallsCompleted() {
		return installs_completed;
	}

	public void setInstallsCompleted(int installsCompleted) {
		this.installs_completed = installsCompleted;
	}

	@JsonProperty("installs_failed")
	public int getInstallsFailed() {
		return installs_failed;
	}

	public void setInstallsFailed(int installsFailed) {
		this.installs_failed = installsFailed;
	}

	@JsonProperty("failed_list")
	public List<String> getFailedList() {
		return failed_list;
	}

	public void setFailedList(List<String> failedList) {
		this.failed_list = failedList;
	}

}
