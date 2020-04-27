package net.thumbtack.thumbnote.thumbnoteroot.spring.form.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteOutputForm {
    private Integer id;
    private String name;
    private List<String> text;
    private String creationTime;

    private List<String> tags;

    public NoteOutputForm(String name, List<String> text) {
        this.name = name;
        this.text = text;
    }

    public NoteOutputForm(Note note) {
        id = note.getId();
        name = note.getName();
        text = note.getTextByParagraphs();
        creationTime = note.getCreationTime().format(DateTimeFormatter.ofPattern("HH:mm dd MMMM yyyy"));
        tags = note.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    }

    public NoteOutputForm(ElasticNote elasticNote) {
        id = elasticNote.getId();
        name = elasticNote.getName();
        tags = new ArrayList<>(elasticNote.getTags());
    }

    public String getTextAsString() {
        return text.stream().map(s -> s = s.concat(System.lineSeparator())).reduce(String::concat).get();
    }
}
