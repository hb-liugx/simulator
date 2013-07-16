/*
 * File:   OsInstallStatus.java
 * Author: Peter Steele
 *
 * Unpublished source code.
 *
 * Copyright (c) 2013, Peaxy, Inc. <engineering@peaxy.net>
 *
 * All rights reserved.
 *
 */
package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlEnum
public enum InitHfStatus {
    INCOMPLETE,
    SUCCESSFUL,
    FAILED,
    TIMED_OUT;
}
