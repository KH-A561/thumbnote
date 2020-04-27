package net.thumbtack.thumbnote.thumbnoteroot.spring.form.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditNoteInputForm {
    private int id;
    private String name;
    private String text;
    private String tags;

    public EditNoteInputForm(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public EditNoteInputForm(NoteOutputForm noteOutputForm) {
        this.id = noteOutputForm.getId();
        this.name = noteOutputForm.getName();
        this.text = noteOutputForm.getTextAsString();
        String tags = "";
        for (int i = 0; i < noteOutputForm.getTags().size(); i++) {
            tags = tags.concat("#" + noteOutputForm.getTags().get(i) + " ");
        }
        this.tags = tags;
    }
}
