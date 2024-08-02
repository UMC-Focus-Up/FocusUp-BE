package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.domain.routine.service.UserRoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine/user")
public class UserRoutineController {
    private final UserRoutineServiceImpl userRoutineService;

    // 유저 루틴 생성하기 POST method - 나중에 userId 삭제 필요
    @PostMapping("/create")
    public Response<Long> createRoutine(@RequestBody UserRoutineRequestDTO.CreateRoutine request, @RequestParam Long userId) {
        Long newUserRoutineId = userRoutineService.createUserRoutine(request, userId); // 임시 userId
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
    @Operation(summary = "전체 상위 루틴 리스트 조회 API")
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
