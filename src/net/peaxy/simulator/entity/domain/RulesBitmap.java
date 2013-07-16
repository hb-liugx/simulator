package net.peaxy.simulator.entity.domain;

import java.io.IOException;
import java.util.BitSet;
import java.util.Map;

public class RulesBitmap extends ConfigData {
	public static final int SCHEMA_VERSION = 1;

	public static final int MAX_RULE_ID = 8192; // 8K

	private int crc;

	private int len;

	private byte[] data;

	private int nextRuleId;

	private int reuseVer;

	private transient BitSet bitmap;

	public RulesBitmap() {
		bitmap = new BitSet();
		setType(ConfigDataType.RulesBitmap);
		setSchemaVersion(SCHEMA_VERSION);
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public BitSet getBitmap() {
		return bitmap;
	}

	public void setBitmap(BitSet bitmap) {
		this.bitmap = bitmap;
	}

	public int getReuseVer() {
		return reuseVer;
	}

	public void setReuseVer(int reuseVer) {
		this.reuseVer = reuseVer;
	}

	/**
	 * Try to use rule ids 1 to MAX_RULE_ID (8192), to minimize bits used in
	 * bitmap. When rules are deleted those ids are left as holes and will be
	 * reused when it wraps at 8k after updating reuseVer. This reuseVer is set
	 * as version number in the RuleCfg object.
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getNextRuleId() {
		return null;
	}

	public void setNextRuleId(int nextRuleId) {
		this.nextRuleId = nextRuleId;
	}

	public boolean isRuleExist(int id) {
		return bitmap.get(id);
	}

	public void removeRule(int ruleId) {
		bitmap.clear(ruleId);
	}

	public void addRule(int ruleId) {
		bitmap.set(ruleId);
	}

	public void prePack() throws IOException {

	}

	public void postUnpack() throws IOException {

		bitmap = new BitSet(len * 8);
		for (int i = 0; i < len; ++i) {
			if (data[i] == 0)
				continue;
			for (int b = 0; b < 8; ++b) {
				if ((data[i] & (1 << b)) != 0) {
					bitmap.set((i * 8) + b);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RulesBitmap [ver=").append(getVersion())
				.append(", len=").append(len).append(", nextRuleId=")
				.append(nextRuleId).append(", reuseVer=").append(reuseVer)
				.append(", bitmap=").append(bitmap).append("]");
		return builder.toString();
	}
}
