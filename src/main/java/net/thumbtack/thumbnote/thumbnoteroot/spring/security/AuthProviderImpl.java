package net.thumbtack.thumbnote.thumbnoteroot.spring.security;

import lombok.Setter;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.AccountRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Setter
public class AuthProviderImpl implements AuthenticationProvider {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<Account> optionalAccount;
        if ((optionalAccount = accountDao.findByLogin(login)).isPresent()) {
            Account account = optionalAccount.get();
            if (!bCryptPasswordEncoder.matches(password, account.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            return new UsernamePasswordAuthenticationToken(account, null, authorities);
        } else {
            throw new IllegalArgumentException();
            //TODO
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
