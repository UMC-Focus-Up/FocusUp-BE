
package com.focusup.domain.level.controller;

import com.focusup.domain.level.converter.LevelHistoryConverter;
import com.focusup.domain.level.dto.LevelResponse;
import com.focusup.domain.level.service.LevelService;
import com.focusup.entity.LevelHistory;
import com.focusup.global.apiPayload.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/level")
public class LevelController {
    private final LevelService levelService;

    @PutMapping("/{userId}")
    @Operation(summary = "레벨 변경 API")
    public Response<LevelResponse.NewLevelResultDTO> changeNewLevel (@PathVariable("userId") Long userId, @RequestParam(name = "level") Long level) {
        // level이 0인 경우, 기존 레벨로 돌아간다고 가정
        if (level == 0) {
            LevelHistory levelHistory = levelService.findLevel(userId);
            return Response.success(LevelHistoryConverter.toLevelResultDTO(levelHistory));
        }

        LevelHistory updatedLevel = levelService.updateLevel(userId, level);
        return Response.success(LevelHistoryConverter.toUpdateLevelResultDTO(updatedLevel));
    }
}
