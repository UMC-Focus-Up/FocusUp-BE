package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class LevelHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private int successCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_level_id")
    private Level newLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 필드 초기화를 위한 생성자(아이디 제외)
    public LevelHistory(Level newLevel, Level level, User user) {
        this.newLevel = newLevel;
        this.level = level;
        this.user = user;
    }

    public int changeLevel(Level newLevel) {
        this.newLevel = newLevel;
        return newLevel.getLevel();
    }
}
