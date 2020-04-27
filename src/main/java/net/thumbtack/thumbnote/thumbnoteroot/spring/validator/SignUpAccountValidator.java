package net.thumbtack.thumbnote.thumbnoteroot.spring.validator;

import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpAccountValidator implements Validator {
    @Autowired
    private AccountDao accountDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return SignUpAccountInputForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpAccountInputForm account = (SignUpAccountInputForm) o;
        if (accountDao.findByLogin(account.getLogin()).isPresent()) {
            errors.rejectValue("login", "", "This login is already in use");
        }
        if (accountDao.findByEmail(account.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already in use");
        }
        if (!account.getPassword().equals(account.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "", "Passwords should match");
        }
    }
}
