package com.cherlshall.butterfly.gateway.feign;

import com.cherlshall.butterfly.util.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by htf on 2020/9/25.
 */
@FeignClient("service-account")
public interface AccountFeignClient {

    @GetMapping("/account")
    UserInfo userInfo(@RequestParam String token);
}
