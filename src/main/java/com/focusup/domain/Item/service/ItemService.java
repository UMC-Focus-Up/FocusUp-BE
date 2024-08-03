package com.focusup.domain.Item.service;

import com.focusup.domain.Item.dto.ItemRequest;
import com.focusup.domain.Item.dto.ItemResponse;

public interface ItemService {
    ItemResponse.StoreInfoDTO getStoreInfo(String oauthId);
    int purchaseItem(String oauthId, ItemRequest.PurchaseDTO purchaseDTO);
    ItemResponse.MyItemListDTO getMyItemList(String oauthId);
    void selectCharacterItem(String oauthId, ItemRequest.selectCharacterItemDTO request);
    void deselectCharacterItem(String oauthIdd);
}
