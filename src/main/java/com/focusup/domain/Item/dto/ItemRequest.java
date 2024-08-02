package com.focusup.domain.Item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ItemRequest {

    @Getter
    public static class PurchaseDTO {
        @NotNull
        Long memberId;
        @NotNull
        Long itemId;
    }

    @Getter
    public static class selectCharacterItemDTO {
        @NotNull
        Long userId;
        @NotNull
        Long itemId;
    }

    @Getter
    public static class deselectCharacterItemDTO {
        @NotNull
        Long userId;
    }
}
