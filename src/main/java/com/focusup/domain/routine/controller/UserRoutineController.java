package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.domain.routine.service.UserRoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
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
        Long userRoutineId = userRoutineService.createUserRoutine(request, userId); // 임시 userId
        return Response.success(userRoutineId);
    }

    // 유저 루틴 삭제하기 DELETE method
    @DeleteMapping("/{userRoutineId}")
    public Response deleteRoutine(@PathVariable Long userRoutineId) {
        userRoutineService.deleteRoutine(userRoutineId);
        return Response.success();
    }
}
