package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Level extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int multiple;

    @Column(nullable = false)
    private int minute;

    // 필드 초기화를 위한 생성자(아이디 제외)
    public Level(int level, int multiple, int minute) {
        this.level = level;
        this.multiple = multiple;
        this.minute = minute;
    }
}
