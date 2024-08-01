package com.focusup.global.config;

import com.focusup.global.security.jwt.JwtTokenFilter;
import com.focusup.global.security.jwt.JwtTokenUtils;
import com.focusup.global.security.oauth.CustomOAuth2UserService;
import com.focusup.global.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenUtils jwtTokenUtils;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/css/**", "/images/**", "/js/**", "/lib/**")
                .requestMatchers("/swagger-ui/**", "/api-docs/**")
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // rest api 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                // request 인증, 인가 설정
                .authorizeHttpRequests(request -> request.
                                requestMatchers("api/user/auth/success", "api/user/login/kakao", "login/oauth2/code/kakao", "api/user/auth/reissue").permitAll()
                .anyRequest().authenticated()
                )

                // oauth2 설정
                .oauth2Login(oauth ->
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))
                        // 로그인 성공 시 핸들러
                        .successHandler(oAuth2SuccessHandler)
                )

                // jwt 관련 설정
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtils), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
