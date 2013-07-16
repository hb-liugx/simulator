package net.peaxy.simulator.entity.domain;

public class OsInstallProgress extends PeaxyBaseDomain {
	private int percent_complete;
	OsInstallStatus status;
	
	public int getPercentComplete() {
		return percent_complete;
	}

	public void setPercentComplete(int percentComplete) {
		this.percent_complete = percentComplete;
	}

	public OsInstallStatus getStatus() {
		return status;
	}

	public void setStatus(OsInstallStatus status) {
		this.status = status;
	}

}
