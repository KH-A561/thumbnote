package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import lombok.Getter;
import lombok.Setter;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@Setter
@Getter
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public Account signUpAccount(SignUpAccountInputForm signUpAccountInputForm, BindingResult result) {
        Account account = new Account(signUpAccountInputForm);
        account.setPassword(bCryptPasswordEncoder.encode(signUpAccountInputForm.getPassword()));
        return accountDao.saveAccount(account);
    }

    @Override
    public Account getCurrentAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
