package com.focusup.domain.Item.controller;

import com.focusup.domain.Item.dto.ItemRequest;
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

    @PostMapping("/purchase")
    @Operation(summary = "상점 아이템 구매 API")
    public Response<?> purchaseItem(@RequestBody ItemRequest.PurchaseDTO request){
        int point = itemService.purchaseItem(request);
        return Response.success(ItemResponse.PurchaseDTO.
                builder()
                .point(point)
                .build());
    }
    @GetMapping("/myitem")
    @Operation(summary = "내 아이템 목록 조회 API")
    public Response<ItemResponse.MyItemListDTO> getMyItemList(@RequestParam Long userId){
        ItemResponse.MyItemListDTO itemList = itemService.getMyItemList(userId);
        return Response.success(itemList);
    }

    @PostMapping("/select")
    @Operation(summary = "캐릭터 아이템 선택 API")
    public Response<?> selectCharacterItem(@RequestBody ItemRequest.selectCharacterItemDTO request){
        itemService.selectCharacterItem(request);
        return Response.success("정상적으로 아이템을 장착하였습니다.");
    }

    @PostMapping("/deselect")
    @Operation(summary = "캐릭터 아이템 삭제(해제) API")
    public Response<?> deselectCharacterItem(@RequestBody ItemRequest.deselectCharacterItemDTO request) {
        itemService.deselectCharacterItem(request.getUserId());
        return Response.success("정상적으로 아이템을 삭제하였습니다.");
    }
}
