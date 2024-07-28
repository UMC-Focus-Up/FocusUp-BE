package com.focusup.domain.Item.service;

import com.focusup.domain.Item.dto.ItemResponse;

public interface ItemService {
    ItemResponse.StoreInfoDTO getStoreInfo(Long userId);
}
