package com.focusup.domain.Item.service;

import com.focusup.domain.Item.dto.ItemResponse;
import com.focusup.domain.Item.repository.ItemRepository;
import com.focusup.domain.Item.repository.OrderRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Item;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public ItemResponse.StoreInfoDTO getStoreInfo(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 존재하지 않을 경우 예외 처리

        // 사용자 포인트 조회
        int point = user.getPoint();

        // 상점 아이템 조회
        List<ItemResponse.StoreItemDTO> items = getItems(userId);

        return ItemResponse.StoreInfoDTO.builder()
                .point(point)
                .itemList(items)
                .build();
    }

    @Override
    public ItemResponse.MyItemListDTO getMyItemList(Long userId) {
        List<Item> items = orderRepository.findItemsByUserId(userId);
        System.out.println("item found: " + items);
        List<ItemResponse.MyItemDTO> itemList = items.stream().map(item ->
                ItemResponse.MyItemDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .type(item.getType())
                        .imageUrl(item.getImageUrl())
                        .build()
        ).collect(Collectors.toList());

        return ItemResponse.MyItemListDTO.builder()
                .itemList(itemList)
                .build();
    }

    private List<ItemResponse.StoreItemDTO> getItems(Long userId) {
        List<Item> items = itemRepository.findAll(); // 모든 아이템

        List<Long> purchasedItemIds = orderRepository.findItemIdsByUserId(userId); // 사용자가 구매한 아이템 목록
        System.out.println("item found: " + purchasedItemIds);

        // 구매 여부 포함한 정보 반환
        return items.stream().map(item ->
                ItemResponse.StoreItemDTO.builder()
                        .id(item.getId())
                        .price(item.getPrice())
                        .name(item.getName())
                        .type(item.getType())
                        .imageUrl(item.getImageUrl())
                        .purchased(purchasedItemIds.contains(item.getId())) // 구매 여부
                        .build()
        ).collect(Collectors.toList());
    }
}
