package com.example.spring_security_6.config;

import com.example.spring_security_6.model.AuthorityEntity;
import com.example.spring_security_6.model.UserDetailsImpl;
import com.example.spring_security_6.model.UserEntity;
import com.example.spring_security_6.model.UserInfoGithub;
import com.example.spring_security_6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserInfoGithub userInfo = (UserInfoGithub) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userInfo.getEmail());
        if(user == null){
            // insert new user
            UserEntity createUser = UserEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .username(userInfo.getName())
                    .password("")
                    .email(userInfo.getEmail())
                    .build();
            // default role
            Set<AuthorityEntity> authorities = Set.of("ROLE_USER").stream().map(s ->
                    AuthorityEntity.builder()
                            .name(s)
                            .user(createUser)
                            .build()
            ).collect(Collectors.toSet());
            createUser.setAuthorities(authorities);
            user = userRepository.save(createUser);
            log.info("Insert new user with email {} successfully", userInfo.getEmail());
        }
        return new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(user), null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
