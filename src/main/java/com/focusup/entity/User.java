package com.focusup.entity;

import com.focusup.entity.base.BaseEntity;
import com.focusup.entity.enums.Role;
import com.focusup.entity.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String oauthId;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    @Builder.Default
    private int life = 5;

    @Column(nullable = false)
    @Builder.Default
    private int point = 0;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item curItem;

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return oauthId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 필드 초기화를 위한 생성자
    public User(String oauthId, SocialType socialType, int life, int point, Item curItem) {
        this.oauthId = oauthId;
        this.socialType = socialType;
        this.life = life;
        this.point = point;
        this.curItem = curItem;
    }

    public void changePoint(int point){
        this.point = point;
    }

    public void changeCurItem(Item item){
        this.curItem = item;
    }
}
