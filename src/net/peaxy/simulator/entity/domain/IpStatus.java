package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlEnum
public enum IpStatus {
	PINGABLE, NOT_PINGABLE, INVALID, NULL
}
