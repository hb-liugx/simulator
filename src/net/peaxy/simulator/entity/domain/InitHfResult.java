/*
 * File:   InitHfResult.java
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

import java.util.List;
import java.util.Map;

public class InitHfResult extends PeaxyBaseDomain {
    Map<Long, Integer> initHfProgress;
    Map<Long, List<String>> initHfLog;
    
    public Map<Long, Integer> getInitHfProgress() {
        return initHfProgress;
    }
    
    public void setInitHfProgress(Map<Long, Integer> initHfProgress) {
        this.initHfProgress = initHfProgress;
    }
    
    public Map<Long, List<String>> getInitHfLog() {
        return initHfLog;
    }
    
    public void setInitHfLog(Map<Long, List<String>> initHfLog) {
        this.initHfLog = initHfLog;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InitHfResult [initHfProgress=").append(initHfProgress)
                .append(", initHfLog=").append(initHfLog).append("]");
        return builder.toString();
    }
}
