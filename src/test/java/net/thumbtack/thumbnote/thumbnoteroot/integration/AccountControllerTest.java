package net.thumbtack.thumbnote.thumbnoteroot.integration;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.ObjectError;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends ControllerTestBase {
    @Test
    public void testSignUpAccount() throws Exception {
        SignUpAccountInputForm signUpAccountInputForm = new SignUpAccountInputForm("qqqwww@qqq.qqq", "Login12345", "123456pass", "123456pass", "Alexander");
        mockMvc.perform(post("/account/signup").flashAttr("signUpAccountInputForm",  signUpAccountInputForm))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/notebooks"))
                .andExpect(model().attribute("account", isA(Account.class)));
    }
}
