package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.domain.routine.service.UserRoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.handler.annotation.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine/user")
public class UserRoutineController {
    private final UserRoutineServiceImpl userRoutineService;

    // 유저 루틴 생성하기 POST method - 나중에 userId 삭제 필요
    @PostMapping("/create")
    public Response<Long> createRoutine(@RequestBody UserRoutineRequestDTO.CreateRoutine request, @Auth String oauthId) {
        Long newUserRoutineId = userRoutineService.createUserRoutine(request, oauthId);
        return Response.success(newUserRoutineId);
    }

    // 유저 루틴 삭제하기 DELETE method
    @DeleteMapping("/{userRoutineId}")
    public Response deleteRoutine(@PathVariable Long userRoutineId) {
        userRoutineService.deleteRoutine(userRoutineId);
        return Response.success();
    }

    // 전체 유저루틴 조회하기 GET method
    @GetMapping("/all")
    public Response<UserRoutineResponseDTO.GetAllUserRoutineList> getAllUserRoutine() {
        UserRoutineResponseDTO.GetAllUserRoutineList allUserRoutineList = userRoutineService.getAllUserRoutineList();
        return Response.success(allUserRoutineList);
    }

    // 유저루틴 상세 조회하기 GET method
    @GetMapping("/{userRoutineId}")
    public Response<UserRoutineResponseDTO.UserRoutineDetail> getAllUserRoutine(@PathVariable Long userRoutineId) {
        UserRoutineResponseDTO.UserRoutineDetail userRoutineDetail = userRoutineService.getUserRoutineDetail(userRoutineId);
        return Response.success(userRoutineDetail);
    }
}
