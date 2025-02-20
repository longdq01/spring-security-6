package com.example.spring_security_6.config;

import com.example.spring_security_6.model.AuthorityEntity;
import com.example.spring_security_6.model.UserDetailsImpl;
import com.example.spring_security_6.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                (requests) -> requests
                        .requestMatchers("/account", "/balance", "/card").authenticated()
                        .requestMatchers(
                                "/notice",
                                "/contact",
                                "/error"
                        ).permitAll()
        );
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // simulated get user in db and convert to UserDetails
        String userId = UUID.randomUUID().toString();
        Set<AuthorityEntity> authorityEntities = new HashSet<>();
        authorityEntities.add(AuthorityEntity.builder().id(1L).name("VIEWACCOUNT").build());
        authorityEntities.add(AuthorityEntity.builder().id(1L).name("VIEWBALANCE").build());

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .email("longdq@gmail.com")
                .username("longdq")
                .password(passwordEncoder.encode("longdq"))
                .authorities(authorityEntities)
                .build();

        return new InMemoryUserDetailsManager(UserDetailsImpl.build(userEntity));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}