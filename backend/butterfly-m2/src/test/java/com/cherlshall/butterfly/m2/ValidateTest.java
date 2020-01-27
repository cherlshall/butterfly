package com.cherlshall.butterfly.m2;

import com.cherlshall.butterfly.common.validate.Validate;
import com.cherlshall.butterfly.m2.entity.po.Protocol;
import org.junit.Test;

public class ValidateTest {
    @Test
    public void testProtocol() {
        Protocol protocol = new Protocol();
        protocol.setCategory(1);
        protocol.setEnName("http");
        protocol.setActive(1);
        Validate.check(protocol);
    }
}
