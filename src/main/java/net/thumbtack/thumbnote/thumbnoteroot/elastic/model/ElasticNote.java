package net.thumbtack.thumbnote.thumbnoteroot.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Document(indexName = "note")
@AllArgsConstructor
@NoArgsConstructor
public class ElasticNote {
    private Integer id;
    private Integer accountId;
    private Set<Integer> notebooksIds;
    private String name;
    @Field(type = FieldType.Keyword)
    private Set<String> tags;
    private List<String> text;

    public ElasticNote(Note note) {
        this.id = note.getId();
        this.accountId = note.getAccount().getId();
        this.notebooksIds = note.getNotebooks().stream().map(Notebook::getId).collect(Collectors.toSet());
        this.name = note.getName();
        if (note.getTags() != null && !note.getTags().isEmpty()) {
            this.tags = note.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
        }
        this.text = note.getTextByParagraphs();
    }
}
