package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate date;

    @Builder.Default
    private int delayCount = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userRoutineId")
    private UserRoutine userRoutine;

    public void changeDelayCount(int count) {
        this.delayCount += count;
    }
}
