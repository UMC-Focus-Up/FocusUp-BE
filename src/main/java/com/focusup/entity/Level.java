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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_history_id")
    private LevelHistory levelHistory; //???????

}
