package net.thumbtack.thumbnote.thumbnoteroot.spring.form.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteInputForm {
    @NotBlank
    private String name;
    @NotBlank
    private String text;

    private String tags;

    public NoteInputForm(@NotBlank String name, @NotBlank String text) {
        this.name = name;
        this.text = text;
    }
}
