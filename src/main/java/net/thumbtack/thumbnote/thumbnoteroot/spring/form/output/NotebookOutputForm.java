package net.thumbtack.thumbnote.thumbnoteroot.spring.form.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.CoverSrcUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotebookOutputForm {
    @NotBlank
    private Integer id;
    @NotNull
    private Account account;
    @NotBlank
    private String title;
    private String description;
    private List<Note> notes;
    private String modificationTime;

    private String coverSrc;


    public NotebookOutputForm(Notebook notebook) {
        id = notebook.getId();
        account = notebook.getAccount();
        title = notebook.getTitle();
        description = notebook.getDescription();
        notes = new LinkedList<>(notebook.getNotes());
        if (notebook.getModificationTime() != null) {
            modificationTime = notebook.getModificationTime().format(DateTimeFormatter.ofPattern("HH:mm dd MMMM yyyy"));
        }
        coverSrc = CoverSrcUtils.generateCover();
    }

    public  List<NoteOutputForm> getNotesAsForms() {
        return  this.notes.stream().map(NoteOutputForm::new).collect(Collectors.toList());
    }
}
