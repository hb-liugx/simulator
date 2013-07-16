package net.peaxy.simulator.entity.domain;

import java.util.Random;

import javax.xml.bind.annotation.XmlElement;

import net.peaxy.simulator.util.Utility;

import org.codehaus.jackson.annotate.JsonProperty;

public class InterfaceInfo extends PeaxyBaseDomain {
	@XmlElement
	String interface_name;
	@XmlElement
	int speed;
	@XmlElement
	boolean is_active;
	@XmlElement
	boolean is_bonded;

	@JsonProperty("interface_name")
	public String getInterfaceName() {
		return interface_name;
	}

	public void setInterfaceName(String interfaceName) {
		this.interface_name = interfaceName;
	}

	@JsonProperty("speed")
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@JsonProperty("is_active")
	public boolean isActive() {
		return is_active;
	}

	public void setActive(boolean isActive) {
		this.is_active = isActive;
	}

	@JsonProperty("is_bonded")
	public boolean isBonded() {
		return is_bonded;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InterfaceInfo [interfaceName=").append(interface_name)
				.append(", speed=").append(speed).append(", isActive=")
				.append(is_active).append(", isBonded=").append(is_bonded)
				.append("]");
		return builder.toString();
	}

	public void setBonded(boolean isBonded) {
		this.is_bonded = isBonded;
	}

	public static InterfaceInfo generate(int d) {
		Random r = new Random();
		InterfaceInfo info = new InterfaceInfo();
		info.setActive(true);
		info.setBonded(true);
		info.setInterfaceName(Utility.generateSN(4));
		info.setSpeed((r.nextInt(5)+1)*10);
		return info;
	}

}
