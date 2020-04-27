package net.thumbtack.thumbnote.thumbnoteroot.spring.form.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpAccountInputForm {
    @NotBlank
    @Email(message = "Please use correct email address")
    private String email;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9_]+")
    @Size(min = 5, max = 20)
    private String login;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9_*!]+", message = "")
    private String password;
    private String matchingPassword;

    @NotBlank
    @Pattern(regexp = "[A-Za-z ]+")
    @Size(max = 20, message = "Name cannot be longer than 20 characters")
    private String name;

    public void setLogin(String login) {
        this.login = login.toLowerCase();
    }
}
