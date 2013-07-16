package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlEnum
public enum HvType {
    UNKNOWN(0),
    KVM(1),
    AWS(2);
    
    private final int value;

    HvType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
    
    public static HvType valueOf(int type) {
        if (type == UNKNOWN.value)
            return UNKNOWN;
        else if (type == KVM.value)
            return KVM;
        else if (type == AWS.value)
            return AWS;
        else
            throw new IllegalArgumentException("illegal enum value:" + type);
    }  
}
