package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import com.focusup.entity.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String accessToken;

    @Column(nullable = false)
    @Builder.Default
    private int life = 5;

    @Column(nullable = false)
    @Builder.Default
    private int point = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item curItem;

    // 필드 초기화를 위한 생성자
    public User(String email, SocialType socialType, int life, int point, Item curItem) {
        this.email = email;
        this.socialType = socialType;
        this.life = life;
        this.point = point;
        this.curItem = curItem;
    }

    public void changePoint(int point){
        this.point = point;
    }
}
