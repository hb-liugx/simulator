package net.peaxy.simulator.entity.domain;

import java.util.Random;

import javax.xml.bind.annotation.XmlElement;

import net.peaxy.simulator.util.Utility;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiskInfo extends PeaxyBaseDomain {
	@XmlElement
	String name;
	@XmlElement
	long size;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("size")
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return name + ":" + size;
	}

	public static DiskInfo generate(int d) {
		DiskInfo disk = new DiskInfo();
		disk.setName("d" + Utility.generateSN(3));
		disk.setSize(new Random(System.nanoTime()).nextInt(2000));
		return disk;
	}
}
