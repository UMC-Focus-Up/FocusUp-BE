package com.focusup.domain.Item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ItemResponse {

    @Builder
    @Getter
    public static class StoreInfoDTO {
        int point;
        List<StoreItemDTO> itemList;
    }

    @Builder
    @Getter
    public static class StoreItemDTO {
        Long id;
        int price;
        String name;
        String type;
        String imageUrl;
        boolean purchased;
    }
}
