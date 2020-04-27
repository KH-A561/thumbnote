package net.thumbtack.thumbnote.thumbnoteroot.spring.form.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogInAccountInputForm {
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9_]+")
    private String login;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9_*!]+")
    private String password;

    public void setLogin(String login) {
        this.login = login.toLowerCase();
    }
}
