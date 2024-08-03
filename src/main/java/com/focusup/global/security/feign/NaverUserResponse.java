package com.focusup.global.security.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NaverUserResponse {
    private Response response;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {
        private String id;
    }
}