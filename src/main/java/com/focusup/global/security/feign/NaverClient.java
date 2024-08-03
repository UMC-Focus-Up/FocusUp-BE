package com.focusup.global.security.feign;

import com.focusup.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "naver-client",
        url = "https://openapi.naver.com",
        configuration = FeignClientConfig.class
)
public interface NaverClient {

    @GetMapping("/v1/nid/me")
    NaverUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);

}
