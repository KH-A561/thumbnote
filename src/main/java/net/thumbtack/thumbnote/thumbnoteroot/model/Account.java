package net.thumbtack.thumbnote.thumbnoteroot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    public Account(@NotBlank String login, @NotBlank String password, @NotBlank String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Account(SignUpAccountInputForm signUpAccountInputForm) {
        this.name = signUpAccountInputForm.getName();
        this.login = signUpAccountInputForm.getLogin();
        this.password = signUpAccountInputForm.getPassword();
        this.email = signUpAccountInputForm.getEmail();
    }
}
