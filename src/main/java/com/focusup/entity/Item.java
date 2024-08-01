package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Item  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String type;

    @Column(nullable = false)
    private String imageUrl;

    // 필드 초기화를 위한 생성자(아이디 제외)
    public Item(int price, String name, String type, String imageUrl) {
        this.price = price;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
    }
}
