package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.service.RoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineServiceImpl routineService;

    // 루틴 완료하기 POST method
    @PostMapping("/{routineId}")
    public Response<Long> finishRoutine(@RequestBody RoutineRequestDTO.FinishRoutine request, @PathVariable Long routineId) {
        Long finishedRoutineId = routineService.finishRoutine(request, routineId);
        return Response.success(finishedRoutineId);
    }

    // 루틴 전체 리스트 조회하기 GET method
    @GetMapping("/all")
    public Response<RoutineResponseDTO.GetAllRoutineList> getAllRoutine() {
        RoutineResponseDTO.GetAllRoutineList response = routineService.getAllRoutineList();
        return Response.success(response);
    }

    // 특정 일의 루틴 리스트 조회하기 GET method
    @GetMapping("")
    public Response<RoutineResponseDTO.GetTodayRoutineList> getTodayRoutine(@RequestParam LocalDate date) {
        RoutineResponseDTO.GetTodayRoutineList response = routineService.getTodayRoutineList(date);
        return Response.success(response);
    }
}
