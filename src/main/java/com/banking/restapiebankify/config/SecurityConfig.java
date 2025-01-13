package com.banking.restapiebankify.config;

import com.banking.restapiebankify.security.JwtTokenFilter;
import com.banking.restapiebankify.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final SecurityService securityService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .httpBasic(Customizer -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())  // Enable/disable CORS as needed
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register",
                                "/api/auth/login",
                                "api/auth/refresh-token",
                                "api/auth/verify",
                                "api/auth/me",
                                "api/bankaccounts/account/**",
                                "api/bankaccounts/all",
                                "api/bankaccounts/myAccounts",
                                "api/bankaccounts/create",
                                "api/bankaccounts/update/**",
                                "api/bankaccounts/delete/**",
                                "api/bankaccounts/admin/**",
                                "api/bankaccounts/admin/activate/**",
                                "api/bankaccounts/admin/block/**",
                                "api/bills/create",
                                "api/bills/all",
                                "api/bills/myBill",
                                "api/bills/pay/**",
                                "api/transactions/account/**",
                                "api/transactions/all",
                                "api/transactions/approve/**",
                                "api/transactions/reject/**",
                                "api/transactions/create",
                                "api/loans/request",
                                "api/loans/approve/**",
                                "api/loans/reject/**",
                                "api/loans/user",
                                "api/loans/all"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
