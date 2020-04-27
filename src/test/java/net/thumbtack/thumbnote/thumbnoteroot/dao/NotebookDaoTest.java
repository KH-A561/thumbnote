package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class NotebookDaoTest extends DaoTestBase {
    @Autowired
    private NotebookDao notebookDao;
    @Autowired
    private AccountDao accountDao;
    @Value("${defaultNotebookName}")
    String defaultNotebookName;


    @Test
    @Transactional
    public void testSaveNotebook() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);
        Notebook notebook = new Notebook(0, "Notebook", "Asd", account, null, new HashSet<>());
        notebookDao.saveNotebook(notebook);
        Notebook actual = notebookDao.getById(notebook.getId());
        Assert.assertEquals(notebook, actual);
    }

    @Test
    @Transactional
    public void testFindAllByAccount() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);

        Set<Notebook> expected = new HashSet<>();
        Notebook notebook = new Notebook(0, "Notebook 1", "Asd", account, null, new HashSet<>());
        expected.add(notebookDao.saveNotebook(notebook));
        notebook = new Notebook(0, "Notebook 2", "Asd", account, null, new HashSet<>());
        expected.add(notebookDao.saveNotebook(notebook));
        notebook = new Notebook(0, "Notebook 3", "Asd", account, null, new HashSet<>());
        expected.add(notebookDao.saveNotebook(notebook));

        Set<Notebook> actual = notebookDao.findAllByAccount(account);
        Assert.assertEquals(actual, expected);
    }

    @Test
    @Transactional
    public void testGetDefaultNotebook() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);

        Notebook notebook = new Notebook(0, defaultNotebookName, "Asd", account, null, new HashSet<>());
        notebookDao.saveNotebook(notebook);
        Notebook expected = notebookDao.getDefaultNotebook(account);
        Assert.assertEquals(notebook, expected);
    }

    @Test
    @Transactional
    public void testUpdateNotebook() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);
        Notebook notebook = new Notebook(0, "Notebook", "Asd", account, null, new HashSet<>());
        notebookDao.saveNotebook(notebook);
        notebook.setTitle(defaultNotebookName);
        Notebook expected = notebookDao.getDefaultNotebook(account);
        Notebook actual = notebookDao.getById(notebook.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testDeleteNotebook() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);

        Notebook notebook = new Notebook(0, "Notebook 1", "Asd", account, null, new HashSet<>());
        notebookDao.saveNotebook(notebook);
        notebook = new Notebook(0, "Notebook 2", "Asd", account, null, new HashSet<>());
        notebookDao.saveNotebook(notebook);
        notebookDao.deleteNotebook(notebook);

        Set<Notebook> actual = notebookDao.findAllByAccount(account);
        Assert.assertEquals(actual.size(), 1);
    }
}
