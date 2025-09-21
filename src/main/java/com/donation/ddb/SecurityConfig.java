package com.donation.ddb;

import com.donation.ddb.Service.TokenService.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .authenticationProvider(authenticationProvider) // 추가
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                //세션 사용 안함

                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

//                //iframe 보안 설정
//                .headers((headers) -> headers
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
//
//                //
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))

//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**","/auth/logout","/api/**","/api/v1/user/sign-up/**" , "/wallet/**","/h2-console/**", "/images/**").permitAll()  // 회원가입, 로그인, 인증 경로는 허용
//
//                        .requestMatchers("/api/test/protected").authenticated()
//                        .anyRequest().authenticated()             // 그 외는 인증 필요
//                )
                .authorizeHttpRequests(auth -> auth
                        // 1) 보호해야 하는 /auth/me를 먼저 명시 (가장 먼저 평가됨)
                        .requestMatchers("/auth/me").authenticated()

                        // 2) 로그인/회원가입 계열은 하위 경로까지 허용
                        .requestMatchers("/auth/login/**", "/auth/signup/**", "/auth/logout").permitAll()

                        // 3) 나머지 퍼블릭 경로
                        .requestMatchers("/h2-console/**", "/images/**", "/wallet/**",
                                "/api/**", "/api/v1/user/sign-up/**").permitAll()

                        // 4) 그 외는 인증 필요
                        .anyRequest().authenticated()
                )


                //JWT 인증 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                      UsernamePasswordAuthenticationFilter.class);


                //JWT 인증 필터를 Spring Security의 기본 인증 필터 이전에 추가함.
                //요청 헤더에서 JWT 토큰 추출하고 검증한 후, 인증 정보를 SecurityContext에 설정
                //다른 Spring Security 필터들이 JWT 토큰으로 인증된 사용자 정보 사용할 수 있음.
        return http.build();
    }

    // CORS 설정을 위한 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(Arrays.asList(// 모든 도메인 허용
        // 허용할 오리진(출처) 설정
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",     // React 기본 포트
                "http://localhost:5500",     // Live Server 기본 포트
                "http://localhost:8000",     // Python 서버 기본 포트
                "http://127.0.0.1:5500",
                "http://172.20.10.9:5500",
                "http://172.20.10.9:3000",
                "http://172.20.10.9:8080",
                "http://172.20.10.9:8000",
                "http://172.20.10.9:5173",
                "http://127.0.0.1:8000",
                "http://localhost:8082", //h2
                "http://192.168.1.107:14281",
                "http://192.168.0.11:8080",
                "http://192.168.0.11:5500",
                "http://192.168.0.11:3000",
                "http://192.168.0.11:5173",
                "http://192.168.56.1:5173"
                ,"http://192.168.56.1:3000",
                "http://192.168.56.1:8000",
                "http://10.101.48.92:3000",
                "http://10.101.48.92:5500",
                "http://10.101.48.92:8080" ,
                "http://10.101.48.92:5173",
                "http://10.101.48.199:3000",
                "http://10.101.48.199:5500",
                "http://10.101.48.199:8080" ,
                "http://10.101.48.199:5173",
                "http://10.101.48.14:3000",
                "http://10.101.48.14:5500",
                "http://10.101.48.14:8080" ,
                "http://10.101.48.14:5173",
                "http://10.101.32.65:3000",
                "http://10.101.32.65:5500",
                "http://10.101.32.65:8080" ,
                "http://10.101.32.65:5173",
                "http://10.101.32.1:3000",
                "http://10.101.32.1:5500",
                "http://10.101.32.1:8080",
                "http://10.101.32.1:5173",
                "http://10.101.32.88:3000",
                "http://10.101.32.88:5500",
                "http://10.101.32.88:8080",
                "http://10.101.32.88:5173",
                "http://192.168.1.100:80",
                "http://192.168.1.100:80",
                 "http://192.168.41.159:3000",
                "http://192.168.41.159:5173",
                "http://192.168.41.159:5500",
                "http://192.168.41.159:8080",
                "http://10.182.244.173:3000",
                "http://10.182.244.173:5173",
                "http://10.182.244.173:5500",
                "http://10.182.244.173:8080",
                "http://10.101.48.16:3000",
                "http://10.101.48.16:5500",
                "http://10.101.48.16:5173",
                "http://10.101.48.16:8080",
                "http://10.101.48.16:5174",
                "http://10.115.8.173:3000",
                "http://10.115.8.173:5500",
                "http://10.115.8.173:5173",
                "http://10.115.8.173:8080",
                "http://10.115.8.173:5174",
                "http://localhost:5173",
                "http://localhost:5174",
                "https://sookchain.vercel.app"
        ));

        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // 자격 증명(쿠키 등) 허용 설정
        configuration.setAllowCredentials(true);

        // 브라우저가 접근할 수 있는 헤더 설정
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        // preflight 요청의 캐시 시간 설정 (초 단위)
        configuration.setMaxAge(3600L);

        //URL별 CORS 정책을 관리하는 관리자 역할
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 URL 요청에 대해 이 CORS 설정(configuration)을 사용하도록 등록
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/images/**");
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
