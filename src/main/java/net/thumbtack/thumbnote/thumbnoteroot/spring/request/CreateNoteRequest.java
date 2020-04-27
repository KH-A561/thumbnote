package net.thumbtack.thumbnote.thumbnoteroot.spring.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoteRequest {
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private Integer notebookId;
    @NotBlank
    private String name;
    @NotBlank
    private String text;

    private String tags;
}
