package net.thumbtack.thumbnote.thumbnoteroot.jpa.dao;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;

import java.util.Set;

public interface NotebookDao {
    Set<Notebook> findAllByAccount(Account account);

    Notebook saveNotebook(Notebook notebook);

    Notebook getById(Integer id);

    Notebook getDefaultNotebook(Account account);

    Notebook updateNotebook(Notebook notebook);

    void deleteNotebook(Notebook notebook);
}
