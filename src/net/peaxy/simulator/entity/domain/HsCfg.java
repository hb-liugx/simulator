package net.peaxy.simulator.entity.domain;

import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.jackson.annotate.JsonProperty;

public class HsCfg extends ConfigData {
	public static final int CARDINALITY = 3;
	public static final int SPARE = 0;

	public static final int SCHEMA_VERSION = 1;

	private int cardinality = CARDINALITY;
	private int spareCount = SPARE;
	private boolean replacementEnabled = true;;

	private static AtomicReference<HsCfg> instance;

	static {
		instance = new AtomicReference<HsCfg>();
	}

	public static HsCfg getInstance() {
		return instance.get();
	}

	public static void setInstance(HsCfg s) {
		instance.set(s);
	}

	public HsCfg() {
		setType(ConfigDataType.HsCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

	@JsonProperty("cardinality")
	public int getCardinality() {
		return cardinality;
	}

	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}

	@JsonProperty("spare_count")
	public int getSpareCount() {
		return spareCount;
	}

	public void setSpareCount(int spareCount) {
		this.spareCount = spareCount;
	}

	@JsonProperty("member_replacement")
	public boolean isReplacementEnabled() {
		return replacementEnabled;
	}

	public void setReplacementEnabled(boolean replacementEnabled) {
		this.replacementEnabled = replacementEnabled;
	}

	@Override
	public String toString() {
		return "HsCfg [cardinality=" + cardinality + ", spareCount="
				+ spareCount + ", replacementEnabled=" + replacementEnabled
				+ "]";
	}
}
