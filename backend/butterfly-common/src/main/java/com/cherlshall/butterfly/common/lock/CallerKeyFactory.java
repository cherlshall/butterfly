package com.cherlshall.butterfly.common.lock;

import java.lang.management.ManagementFactory;

/**
 * Created by htf on 2020/9/24.
 */
public class CallerKeyFactory {

    private static Integer processId;

    public static CallerKey produce() {
        return new CallerKey(1, getProcessId(), Thread.currentThread().getId());
    }

    private static Integer getProcessId() {
        if (processId == null) {
            synchronized (CallerKeyFactory.class) {
                if (processId == null) {
                    processId = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
                }
            }
        }
        return processId;
    }
}
