package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import lombok.Setter;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.TagDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.EditNoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.CollectionsUtils;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.TagFormUtils;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Setter
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NotebookDao notebookDao;
    @Autowired
    private NoteElasticDao noteElasticDao;
    @Autowired
    private NoteDao noteDao;
    @Autowired
    private TagDao tagDao;
    @Value("${defaultNotebookName}")
    String defaultNotebookName;

    public NoteOutputForm addNote(Account account, Notebook notebook, NoteInputForm noteInputForm) {
        Notebook defaultNotebook = notebookDao.getDefaultNotebook(account);
        Note note = new Note();
        if (noteInputForm.getTags() != null && !noteInputForm.getTags().isEmpty()) {
            Set<String> tagNames = TagFormUtils.getTagNamesAsSet(noteInputForm.getTags());
            Set<Tag> tags = tagDao.findTagsByNames(tagNames).stream().
                                   filter(tag -> (Optional.ofNullable(tag.getAccount()).isPresent() && tag.getAccount().equals(account))).collect(Collectors.toSet());
            Set<String> existingTagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
            tagNames.removeAll(existingTagNames);
            for (String tagName : tagNames) {
                tags.add(new Tag(tagName, account, CollectionsUtils.convertArrayToSet(note)));
            }
            note.setTags(tags);
        }
        note.setAccount(account);
        note.setNotebooks(CollectionsUtils.convertArrayToSet(defaultNotebook, notebook));
        note.setName(noteInputForm.getName());
        note.setCreationTime(LocalDateTime.now());
        note.setText(noteInputForm.getText());
        noteDao.saveNote(note);
        if (notebook.getNotes() == null) {
            Set<Note> notes = new HashSet<>();
            notes.add(note);
            notebook.setNotes(notes);
        } else {
            notebook.getNotes().add(note);
        }
        notebook.setModificationTime(note.getCreationTime());
        defaultNotebook.setModificationTime(note.getCreationTime());
        defaultNotebook.getNotes().add(note);
        notebookDao.updateNotebook(notebook);
        notebookDao.updateNotebook(defaultNotebook);
        noteElasticDao.saveNote(new ElasticNote(note));
        return new NoteOutputForm(note);
    }

    public NoteOutputForm updateNote(Account account, Integer id, Notebook notebook, EditNoteInputForm noteInputForm) {
        Note oldNote = noteDao.getNoteById(id);
        if (!oldNote.getAccount().equals(account)) {
            throw new IllegalArgumentException();
            //TODO: ServerException()
        }
        Note newNote = new Note();
        newNote.setAccount(oldNote.getAccount());
        newNote.setId(oldNote.getId());
        newNote.setCreationTime(oldNote.getCreationTime());
        newNote.setModificationTime(LocalDateTime.now());
        if (noteInputForm.getName() == null || noteInputForm.getName().isEmpty()) {
            newNote.setName(oldNote.getName());
        } else {
            newNote.setName(noteInputForm.getName());
        }
        if (noteInputForm.getText() == null || noteInputForm.getText().isEmpty()) {
            newNote.setText(oldNote.getText());
        } else {
            newNote.setText(noteInputForm.getText());
        }
        Set<String> tagNames = TagFormUtils.getTagNamesAsSet(noteInputForm.getTags());
        Set<Tag> tags = tagDao.findTagsByNames(tagNames).stream().
                        filter(tag -> (Optional.ofNullable(tag.getAccount()).isPresent() && tag.getAccount().equals(account))).collect(Collectors.toSet());
        Set<String> existingTagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        tagNames.removeAll(existingTagNames);
        for (String tagName : tagNames) {
            tags.add(new Tag(tagName, account, Collections.singleton(newNote)));
        }
        newNote.setTags(tags);
        newNote.setNotebooks(oldNote.getNotebooks());
        newNote = noteDao.updateNote(newNote);
        noteElasticDao.saveNote(new ElasticNote(newNote));
        notebook.setModificationTime(newNote.getModificationTime());
        notebookDao.updateNotebook(notebook);
        return new NoteOutputForm(newNote);
    }

    @Override
    public Set<NoteOutputForm> getAllNotesByAccount(Account account) {
        Set<Note> notes = noteDao.findAllByAccountId(account.getId());
        Set<NoteOutputForm> result = new HashSet<>();
        for (Note note : notes) {
            result.add(new NoteOutputForm(note));
        }
        return result;
    }

    @Override
    public void deleteNote(Account account, Notebook notebook, Integer id) {
        Note note = noteDao.getNoteById(id);
        if (!note.getAccount().equals(account)) {
            throw new IllegalArgumentException();
            //TODO: ServerException()
        }
        for (Notebook notebookT : note.getNotebooks()) {
            notebookT.getNotes().remove(note);
            notebookDao.updateNotebook(notebookT);
        }
        noteElasticDao.deleteById(note.getId());
        noteDao.deleteById(id);
    }

    @Override
    public Set<NoteOutputForm> getAllNotesFromNotebookByName(Account account, Notebook notebook, String searchInput) {
        Set<ElasticNote> elasticResult = noteElasticDao.findAllByNameInsideNotebook(searchInput, notebook, account);
        Set<Note> notes = noteDao.findAllByIdIn(elasticResult.stream().map(ElasticNote::getId).collect(Collectors.toSet()));
        return notes.stream().map(NoteOutputForm::new).collect(Collectors.toSet());
    }

    @Override
    public Set<NoteOutputForm> getAllNotesFromNotebookByTags(Account account, Notebook notebook, Set<String> tags) {
        Set<ElasticNote> elasticResult = noteElasticDao.findAllByTagsInsideNotebook(tags, notebook, account);
        Set<Note> notes = noteDao.findAllByIdIn(elasticResult.stream().map(ElasticNote::getId).collect(Collectors.toSet()));
        return notes.stream().map(NoteOutputForm::new).collect(Collectors.toSet());
    }

    @Override
    public Set<NoteOutputForm> getAllNotesFromNotebookByText(Account account, Notebook notebook, String searchInput) {
        Set<ElasticNote> elasticResult = noteElasticDao.findAllByTextInsideNotebook(searchInput, notebook, account);
        Set<Note> notes = noteDao.findAllByIdIn(elasticResult.stream().map(ElasticNote::getId).collect(Collectors.toSet()));
        return notes.stream().map(NoteOutputForm::new).collect(Collectors.toSet());
    }

    @Override
    public void moveNoteToNotebook(Account account, Integer noteId, Notebook fromNotebook, Notebook toNotebook) {
        Note note = noteDao.getNoteById(noteId);
        if (!fromNotebook.getTitle().equals(defaultNotebookName)) {
            fromNotebook.getNotes().remove(note);
            notebookDao.updateNotebook(fromNotebook);
        }
        if (!toNotebook.getNotes().add(note)) {
            throw new IllegalArgumentException("The notebook already contains this note");
        }
        notebookDao.updateNotebook(toNotebook);
    }
}
