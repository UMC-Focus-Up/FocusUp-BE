package com.focusup.domain.user.controller;

import com.focusup.domain.user.service.UserService;
import com.focusup.global.apiPayload.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    @Operation(summary = "홈 조회 api")
    public Response<?> getHomeInfo(@RequestParam Long userId){
        return Response.success(userService.getHomeInfo(userId));
    }

    @GetMapping("/character")
    @Operation(summary = "캐릭터 화면 조회")
    public Response<?> getCharacterPageInfo(@RequestParam Long userId){
        return Response.success(userService.getCharacterPageInfo(userId));
    }
}
