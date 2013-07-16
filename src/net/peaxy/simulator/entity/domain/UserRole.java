package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum UserRole {
	ADMIN, REGULAR, UNKNOWN;
}
