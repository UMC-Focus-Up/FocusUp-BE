package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
public class UserRoutine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "user_routine_days", joinColumns = @JoinColumn(name = "user_routine_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> repeatCycleDay = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime goalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Singular
    @OneToMany(mappedBy = "userRoutine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Routine> routines = new ArrayList<>();
}
