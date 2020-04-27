package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.AccountRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
@Setter
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Account> optionalAccount;
        if ((optionalAccount = accountDao.findByLogin(login)).isPresent()) {
            Account account = optionalAccount.get();
            log.debug("User {} loaded successfully", account.getLogin());
            return new User(account.getLogin(), account.getPassword(), new ArrayList<>());
        } else {
            throw new IllegalArgumentException();
            //TODO
        }
    }
}
