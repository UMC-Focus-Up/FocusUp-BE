package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.service.RoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineServiceImpl routineService;

    // 루틴 생성하기 POST method - 나중에 userId 삭제 필요
    @PostMapping("/create")
    public Response<Long> createRoutine(@RequestBody RoutineRequestDTO.CreateRoutine request, @RequestParam Long userId) {
        Long userRoutineId = routineService.createUserRoutine(request, userId); // 임시 userId
        return Response.success(userRoutineId);
    }

    // 루틴 완료하기 POST method
    @PostMapping("/{routineId}")
    public Response<Long> finishRoutine(@RequestBody RoutineRequestDTO.FinishRoutine request, @PathVariable Long routineId) {
        Long finishedRoutineId = routineService.finishRoutine(request, routineId);
        return Response.success(finishedRoutineId);
    }

    // 루틴 삭제하기 DELETE method
    @DeleteMapping("/{userRoutineId}")
    public Response deleteRoutine(@PathVariable Long userRoutineId) {
        routineService.deleteRoutine(userRoutineId);
        return Response.success();
    }

    // 루틴 전체 리스트 조회하기 GET method
    @GetMapping("/all")
    public Response<RoutineResponseDTO.GetAllRoutineList> getAllRoutine() {
        return null;
    }

    // 특정 일의 루틴 리스트 조회하기 GET method
    @GetMapping("")
    public Response<RoutineResponseDTO.GetTodayRoutineList> getTodayRoutine(@RequestParam String date) {
        return null;
    }
}
