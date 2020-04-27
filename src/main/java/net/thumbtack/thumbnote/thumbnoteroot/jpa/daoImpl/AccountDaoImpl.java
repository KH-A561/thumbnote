package net.thumbtack.thumbnote.thumbnoteroot.jpa.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.AccountRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AccountDaoImpl implements AccountDao {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountDaoImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account saveAccount(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataAccessException e) {
            log.error("Can't insert account {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        try {
            return accountRepository.findByLogin(login);
        } catch (DataAccessException e) {
            log.error("Can't get account {}", e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        try {
            return accountRepository.findByEmail(email);
        } catch (DataAccessException e) {
            log.error("Can't get account {}", e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}
