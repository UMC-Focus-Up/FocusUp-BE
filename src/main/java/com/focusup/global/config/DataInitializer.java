package com.focusup.global.config;

import com.focusup.domain.Item.repository.ItemRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Item;
import com.focusup.entity.User;
import com.focusup.entity.enums.SocialType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public DataInitializer(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @PostConstruct
    public void init() {
        // 더미 유저
        if(userRepository.count() == 0) {
            userRepository.save(new User("user@naver.com", SocialType.NAVER, 5, 400, null));
        }

        // 아이템 목록
        if (itemRepository.count() == 0) {
            itemRepository.saveAll(Arrays.asList(
                new Item (150, "조개껍데기", "소지품", "/images/item1_seashell.png"),
                new Item (150, "불가사리", "소지품", "/images/item2_starfish.png"),
                new Item (150, "물고기", "소지품", "/images/item3_fish.png"),
                new Item (200, "리본", "목", "/images/item4_ribbon.png"),
                new Item (200, "흰색 꽃", "머리", "/images/item5_flower.png"),
                new Item (200, "꽃", "머리", "/images/item6_flower.png"),
                new Item (200, "불가사리", "머리", "/images/item7_starfish.png"),
                new Item (300, "모자", "머리", "/images/item8_hat.png"),
                new Item (250, "안경", "눈", "/images/item9_glasses.png"),
                new Item (250, "선글라스", "눈", "/images/item10_sunglasses.png"),
                new Item (300, "물고기", "배경", "/images/item11_fish.png"),
                new Item (300, "불가사리", "배경", "/images/item12_starfish.png"),
                new Item (300, "문어", "배경", "/images/item13_octopus.png"),
                new Item (200, "바위", "배경", "/images/item14_rock.png"),
                new Item (500, "생명권", null, "/images/item15_life.png"),
                new Item (2000, "부활권", null, "/images/item16_resurrection.png")
            ));
        }
    }
}