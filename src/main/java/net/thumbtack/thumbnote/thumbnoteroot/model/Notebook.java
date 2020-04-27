package net.thumbtack.thumbnote.thumbnoteroot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NotebookInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Account account;

    private LocalDateTime modificationTime;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade={PERSIST, MERGE, REFRESH, DETACH})
    @JoinTable(name = "notebook_notes",
            joinColumns = @JoinColumn(name = "notebook_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"))
    private Set<Note> notes;

    public Notebook(Account account, NotebookInputForm notebookInputForm, Set<Note> notes) {
        title = notebookInputForm.getTitle();
        description = notebookInputForm.getDescription();
        this.account = account;
        if (notes != null) {
            this.notes = notes;
        } else {
            this.notes = new HashSet<>();
        }
    }

    public Notebook(NotebookOutputForm notebook) {
        this.id = notebook.getId();
        this.title = notebook.getTitle();
        this.description = notebook.getDescription();
        this.account = notebook.getAccount();
        if (notebook.getModificationTime() != null) {
            this.modificationTime = LocalDateTime.parse(notebook.getModificationTime(), DateTimeFormatter.ofPattern("HH:mm dd MMMM yyyy"));
        }
        if (notebook.getNotes() != null) {
            this.notes = new HashSet<>(notebook.getNotes());
        } else {
            this.notes = new HashSet<>();
        }
    }
}
