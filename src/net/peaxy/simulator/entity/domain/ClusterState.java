package net.peaxy.simulator.entity.domain;

public enum ClusterState {
	READ_SERVICE_STATE, 
	READ_CLUSTER_CONFIG, 
	READ_HS_CONFIG, 
	READ_VIEW, 
	BOOT_GEN, 
	WAIT_MEMBERSHIP_BITMNAP, 
	WAIT_MAJORITY, 
	CREATE_HS, 
	WRITE_DHT, 
	CREATE_HS_NODES, 
	BROADCAST_SC_MAP, 
	START_HS, 
	FD
}
