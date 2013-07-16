package net.peaxy.simulator.entity.domain;

import java.io.ByteArrayInputStream;

public class License extends PeaxyBaseDomain {

	protected String filename;
	protected byte type;
	protected String description;
	protected long initiatedDate;
	protected long expireDate;
	protected long capacity;
	protected String support;
	protected long additionalFeatures;

	public void getLicense(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		bais.available();
	}
}
