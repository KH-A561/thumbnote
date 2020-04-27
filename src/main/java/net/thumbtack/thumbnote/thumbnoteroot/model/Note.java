package net.thumbtack.thumbnote.thumbnoteroot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime creationTime;

    private LocalDateTime modificationTime;

    @NotNull
    private String text;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade={PERSIST, MERGE, REFRESH, DETACH})
    @JoinTable(name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;

    @ManyToMany(mappedBy = "notes")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Notebook> notebooks;

    public Note(@NotNull String name, @NotNull LocalDateTime creationTime, @NotNull String text) {
        this.id = 0;
        this.account = null;
        this.name = name;
        this.creationTime = creationTime;
        this.text = text;
        this.tags = new HashSet<>();
        this.notebooks = new HashSet<>();
    }

    public Note(@NotNull String name, @NotNull LocalDateTime creationTime, @NotNull String text, Set<Tag> tags) {
        this.id = 0;
        this.account = null;
        this.name = name;
        this.creationTime = creationTime;
        this.text = text;
        this.tags = tags;
        this.notebooks = new HashSet<>();
    }

    public List<String> getTextByParagraphs() {
        List<String> result = Arrays.asList(text.split(System.lineSeparator()));
        result = result.stream().map(s -> s = s.trim()).collect(Collectors.toList());
        result.removeIf(String::isEmpty);
        return result;
    }
}
