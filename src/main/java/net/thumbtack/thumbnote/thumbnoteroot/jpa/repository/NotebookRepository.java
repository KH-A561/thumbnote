package net.thumbtack.thumbnote.thumbnoteroot.jpa.repository;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface NotebookRepository extends Repository<Notebook, Integer> {
    void deleteAll();

    Set<Notebook> findAllByAccount(Account account);

    Notebook save(Notebook notebook);

    Notebook saveAndFlush(Notebook notebook);

    Notebook getById(Integer id);

    Set<Notebook> getByTitleAndAccount(@Param("title") String title, @Param("account") Account account);

    void delete(Notebook notebook);
}
