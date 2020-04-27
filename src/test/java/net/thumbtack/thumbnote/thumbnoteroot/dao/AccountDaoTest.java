package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.integration.ControllerTestBase;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountDaoTest extends DaoTestBase {
    @Autowired
    private AccountDao accountDao;

    @Test
    public void testSaveAccount() {
        Account expected = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        Account actual = accountDao.saveAccount(expected);
        Assert.assertNotEquals(actual.getId(), 0);
        Assert.assertEquals(expected, actual);
    }
}
