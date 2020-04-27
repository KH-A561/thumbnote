package net.thumbtack.thumbnote.thumbnoteroot.jpa.dao;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;

import java.util.Optional;

public interface AccountDao {
    Account saveAccount(Account account);

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);
}
