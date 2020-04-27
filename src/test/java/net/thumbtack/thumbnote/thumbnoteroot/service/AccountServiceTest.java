package net.thumbtack.thumbnote.thumbnoteroot.service;

import net.thumbtack.thumbnote.thumbnoteroot.integration.ControllerTestBase;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.AccountServiceImpl;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.SecurityServiceImpl;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.UserDetailsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest extends ServiceTestBase {
    @Autowired
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountDao accountDaoMock;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceMock;
    @Autowired
    private SecurityServiceImpl securityService;

    @BeforeTestClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSignUpAccount() {
        SignUpAccountInputForm signUpAccountInputForm = new SignUpAccountInputForm("qqq@qqq.qqq", "Login123", "pass123456", "pass123456", "John");
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountDaoMock.saveAccount(any(Account.class))).thenReturn(account);
        accountService.setAccountDao(accountDaoMock);

        Account actual = accountService.signUpAccount(signUpAccountInputForm, null);
        Assert.assertEquals(account, actual);
        verify(accountDaoMock).saveAccount(accountArgumentCaptor.capture());
        Account capture = accountArgumentCaptor.getValue();
        Assert.assertTrue(bCryptPasswordEncoder.matches(signUpAccountInputForm.getPassword(), capture.getPassword()));
    }

    @Test
    public void testGetCurrentAccount() {
        Account accountEncoded = new Account(0, "John", "Login123", bCryptPasswordEncoder.encode("pass123456"), "qqq@qqq.qqq");
        Account account = new Account(0, "John", "Login123", "pass123456", "qqq@qqq.qqq");

        when(accountDaoMock.saveAccount(any(Account.class))).thenReturn(accountEncoded);
        when(accountDaoMock.findByLogin(account.getLogin())).thenReturn(Optional.of(accountEncoded));
        when(userDetailsServiceMock.loadUserByUsername(account.getLogin())).thenReturn(new User(account.getLogin(), account.getPassword(), new ArrayList<>()));

        securityService.setUserDetailsService(userDetailsServiceMock);
        securityService.getAuthProvider().setAccountDao(accountDaoMock);
        accountService.setAccountDao(accountDaoMock);

        SignUpAccountInputForm signUpAccountInputForm = new SignUpAccountInputForm("qqq@qqq.qqq", "Login123", "pass123456", "pass123456", "John");
        Account expected = accountService.signUpAccount(signUpAccountInputForm, null);
        securityService.autoLogin(account.getLogin(), account.getPassword());
        Account actual = accountService.getCurrentAccount();
        Assert.assertEquals(expected, actual);
    }
}
