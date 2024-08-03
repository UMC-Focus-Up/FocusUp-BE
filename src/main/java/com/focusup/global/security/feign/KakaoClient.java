package com.focusup.global.security.feign;
import com.focusup.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "kakao-client",
        url = "https://kapi.kakao.com",
        configuration = {FeignClientConfig.class}
)
public interface KakaoClient {
    @GetMapping("/v2/user/me")
    KakaoUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);

}
