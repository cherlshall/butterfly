package com.cherlshall.butterfly.common.lock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by htf on 2020/9/24.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class CallerKey {
    private Integer machineId;
    private Integer processId;
    private Long threadId;
}
