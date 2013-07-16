package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class MigrationPolicyAndSchedule extends PeaxyBaseDomain implements DataPolicy {

	public static MigrationPolicyAndSchedule create(){
		MigrationPolicyAndSchedule mpas = new MigrationPolicyAndSchedule();
		MigrationPolicy policy = new MigrationPolicy();
		Condition co = new Condition();
		co.setDir("/sc/dir/");
		co.setExtension("condition:ext");
		co.setUnAccessedMinutes(342);
		co.setUnModifiedMinutes(3432);
		co.setCurrentSC(1);
		policy.setCondition(co);
		policy.setDescription("policy:desc.");
		policy.setDestination(24);
		policy.setId(1);
		policy.setName("policy1");
		mpas.setPolicy(policy);
		SimpleSchedule sc = new SimpleSchedule();
		sc.setInterval(10);
		sc.setStart(60L);
		mpas.setSchedule(sc);
		return mpas;
	}
	
	private MigrationPolicy policy;
	
	private SimpleSchedule schedule;

	@JsonProperty("policy")
	public MigrationPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(MigrationPolicy policy) {
		this.policy = policy;
	}

	@JsonProperty("schedule")
	public SimpleSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(SimpleSchedule schedule) {
		this.schedule = schedule;
	}
}
