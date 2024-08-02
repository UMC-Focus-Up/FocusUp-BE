package com.focusup.domain.Item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ItemRequest {

    @Getter
    public static class PurchaseDTO {
        @NotNull
        Long itemId;
    }

    @Getter
    public static class selectCharacterItemDTO {
        @NotNull
        Long itemId;
    }

}
