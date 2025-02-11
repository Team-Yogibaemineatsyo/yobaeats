package com.sparta.yobaeats.global.security.authentication;

import com.sparta.yobaeats.global.exception.error.ErrorCode;
import com.sparta.yobaeats.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails userDetailsCustom = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetailsCustom.getPassword())) {
            log.warn("login error: password mismatch");
            throw new BadCredentialsException(ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return new UsernamePasswordAuthenticationToken(userDetailsCustom, null, userDetailsCustom.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
