package com.cherlshall.butterfly.module.basic;

import com.cherlshall.butterfly.module.basic.service.UserService;
import com.cherlshall.butterfly.common.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    public void getUsers() {
        ResponseVO responseVO = userService.login("admin", "111111");
        System.out.println(responseVO);
    }

}
