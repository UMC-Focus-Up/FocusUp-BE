package com.focusup.domain.Item.service;

import com.focusup.domain.Item.dto.ItemRequest;
import com.focusup.domain.Item.dto.ItemResponse;
import com.focusup.domain.Item.repository.ItemRepository;
import com.focusup.domain.Item.repository.OrderRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Item;
import com.focusup.entity.Order;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import jakarta.transaction.Transactional;
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
    public ItemResponse.StoreInfoDTO getStoreInfo(String oauthId) {

        // 사용자 조회
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 존재하지 않을 경우 예외 처리

        // 사용자 포인트 조회
        int point = user.getPoint();

        // 상점 아이템 조회
        List<ItemResponse.StoreItemDTO> items = getItems(oauthId);

        return ItemResponse.StoreInfoDTO.builder()
                .point(point)
                .itemList(items)
                .build();
    }

    @Transactional
    @Override
    public int purchaseItem(String oauthId, ItemRequest.PurchaseDTO purchaseDTO) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자가 존재하지 않을 경우 예외 처리
        int userPoint = user.getPoint();
        Item item = itemRepository.findById(purchaseDTO.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND)); // 아이템이 존재하지 않을 경우 예외 처리
        int price = item.getPrice();
        if(price <= userPoint){
            Order order = Order.builder()
                    .item(item)
                    .user(user)
                    .build();
            orderRepository.save(order);
            userPoint -= price;
            user.changePoint(userPoint);
            return userPoint;
        }
        else{
            throw(new CustomException(ErrorCode.INSUFFICIENT_BALANCE));
        }
    }

    @Override
    public ItemResponse.MyItemListDTO getMyItemList(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Item> items = orderRepository.findItemsByUserId(user.getId());
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

    @Transactional
    @Override
    public void selectCharacterItem(String oauthId, ItemRequest.selectCharacterItemDTO request) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자가 존재하지 않을 경우 예외 처리

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND)); // 아이템이 존재하지 않을 경우 예외 처리

        user.changeCurItem(item);
    }

    @Transactional
    @Override
    public void deselectCharacterItem(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자가 존재하지 않을 경우 예외 처리

        user.changeCurItem(null);
    }

    private List<ItemResponse.StoreItemDTO> getItems(String oauthId) {
        List<Item> items = itemRepository.findAll(); // 모든 아이템

        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Long> purchasedItemIds = orderRepository.findItemIdsByUserId(user.getId()); // 사용자가 구매한 아이템 목록

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
