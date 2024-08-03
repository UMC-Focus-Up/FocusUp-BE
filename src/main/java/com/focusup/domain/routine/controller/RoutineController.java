package com.focusup.domain.routine.controller;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.service.RoutineServiceImpl;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.handler.annotation.Auth;
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
    public Response<Long> finishRoutine(@RequestBody RoutineRequestDTO.FinishRoutine request, @PathVariable Long routineId, @Auth String oauthId) {
        Long finishedRoutineId = routineService.finishRoutine(request, routineId, oauthId);
        return Response.success(finishedRoutineId);
    }

    // 마이페이지 조회 GET method
    @GetMapping("/mypage")
    public Response<RoutineResponseDTO.MyPage> getMyPage(@Auth String oauthId) {
        RoutineResponseDTO.MyPage response = routineService.getMyPage(oauthId);
        return Response.success(response);
    }
}
