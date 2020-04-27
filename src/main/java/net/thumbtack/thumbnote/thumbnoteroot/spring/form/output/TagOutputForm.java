package net.thumbtack.thumbnote.thumbnoteroot.spring.form.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagOutputForm {
    Integer id;
    @NotBlank
    String name;
    @NotNull
    Integer noteCount;
    Set<NoteOutputForm> notes;

    public TagOutputForm(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.noteCount = tag.getNotes().size();
        if (tag.getNotes() != null) {
            this.notes = tag.getNotes().stream().map(NoteOutputForm::new).collect(Collectors.toSet());
        } else {
            this.notes = new HashSet<>();
        }
    }

    public TagOutputForm(@NotBlank String name, Integer noteCount) {
        this.name = name;
        this.noteCount = noteCount;
    }
}
