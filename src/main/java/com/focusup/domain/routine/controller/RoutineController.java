package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.global.apiPayload.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {
    // 루틴 생성하기 POST method
    @PostMapping("/create")
    public Response<Long> createRoutine(@RequestBody RoutineRequestDTO.CreateRoutine request) {
        return null;
    }

    // 루틴 완료하기 POST method
    @PostMapping("/{routineId}")
    public Response<Long> finishRoutine(@PathVariable Long routineId) {
        return null;
    }

    // 루틴 삭제하기 DELETE method
    @DeleteMapping("/{routineId}")
    public Response<String> deleteRoutine(@PathVariable Long routineId) {
        return null;
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
