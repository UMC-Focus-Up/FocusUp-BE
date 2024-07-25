
package com.focusup.domain.level.controller;

import com.focusup.domain.level.converter.LevelHistoryConverter;
import com.focusup.domain.level.dto.LevelResponse;
import com.focusup.domain.level.service.LevelService;
import com.focusup.entity.LevelHistory;
import com.focusup.global.apiPayload.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/level")
public class LevelController {
    private final LevelService levelService;

    @PutMapping("/{userId}")
    @Operation(summary = "레벨 변경 API")
    public Response<LevelResponse.NewLevelResultDTO> putNewLevel (@PathVariable("userId") Long userId, @RequestParam(name = "level") Long level) {
        if (level == 0) {
            LevelHistory levelHistory = levelService.findLevel(userId);
            return Response.success(LevelHistoryConverter.toLevelResultDTO(levelHistory));
        }

        LevelHistory updatedLevel = levelService.updateLevel(userId, level);
        return Response.success(LevelHistoryConverter.toUpdateLevelResultDTO(updatedLevel));
    }
}
