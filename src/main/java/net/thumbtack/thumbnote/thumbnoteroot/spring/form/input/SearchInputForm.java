package net.thumbtack.thumbnote.thumbnoteroot.spring.form.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchInputForm {
    private String searchInput;
}
