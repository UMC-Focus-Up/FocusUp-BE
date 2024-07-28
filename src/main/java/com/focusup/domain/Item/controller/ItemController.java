package com.focusup.domain.Item.controller;

import com.focusup.domain.Item.dto.ItemResponse;
import com.focusup.domain.Item.service.ItemService;
import com.focusup.global.apiPayload.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/store")
    @Operation(summary = "상점 아이템 목록 조회 API")
    public Response<ItemResponse.StoreInfoDTO> getStoreItemList(@RequestParam Long userId){
        ItemResponse.StoreInfoDTO storeInfo = itemService.getStoreInfo(userId);
        return Response.success(storeInfo);
    }

    @GetMapping("/myitem")
    @Operation(summary = "내 아이템 목록 조회 API")
    public Response<ItemResponse.MyItemListDTO> getMyItemList(@RequestParam Long userId){
        ItemResponse.MyItemListDTO itemList = itemService.getMyItemList(userId);
        return Response.success(itemList);
    }

}
