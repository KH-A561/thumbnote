package net.thumbtack.thumbnote.thumbnoteroot.spring.service;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import org.springframework.validation.BindingResult;

public interface AccountService {
    Account signUpAccount(SignUpAccountInputForm signUpAccountInputForm, BindingResult result);
    Account getCurrentAccount();
}
