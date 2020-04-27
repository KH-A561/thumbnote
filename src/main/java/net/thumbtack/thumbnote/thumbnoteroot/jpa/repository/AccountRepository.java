package net.thumbtack.thumbnote.thumbnoteroot.jpa.repository;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AccountRepository extends Repository<Account, Integer> {
    Account save(Account account);

    Optional<Account> findById(Integer id);

    void delete(Account account);

    void deleteAll();

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);
}
