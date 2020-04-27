package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.spring.security.AuthProviderImpl;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Setter
@Getter
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private AuthProviderImpl authProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void autoLogin(String login, String password) {
        log.debug("Authentication started");
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password);
        Authentication authentication = authProvider.authenticate(usernamePasswordAuthenticationToken);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug(String.format("Auto login %s successfully!", login));
        }
    }
}
