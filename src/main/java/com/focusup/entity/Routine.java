package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
public class Routine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private LocalTime execTime = LocalTime.of(0, 0);

    @Column(nullable = false)
    @Builder.Default
    private double achieveRate = 0;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime goalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userRoutineId")
    private UserRoutine userRoutine;
}
