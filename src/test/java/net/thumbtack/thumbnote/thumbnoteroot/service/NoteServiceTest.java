package net.thumbtack.thumbnote.thumbnoteroot.service;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
import net.thumbtack.thumbnote.thumbnoteroot.integration.ControllerTestBase;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.EditNoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.CollectionsUtils;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.AccountServiceImpl;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.NoteServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NoteServiceTest extends ServiceTestBase {
    @Autowired
    @InjectMocks
    private NoteServiceImpl noteService;
    @Mock
    private NoteDao noteDaoMock;
    @Mock
    private NoteElasticDao noteElasticDaoMock;
    @Mock
    private NotebookDao notebookDaoMock;

    @Autowired
    private AccountServiceImpl accountService;

    @BeforeTestClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveNote() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountService.getAccountDao().saveAccount(account);
        NoteInputForm noteInputForm = new NoteInputForm("Note 1", "Note 1 Text", "#tag 1 #tag 2 #tag 3");
        Note note = new Note("Note 1", LocalDateTime.now(), "Note 1 Text", new HashSet<>());
        Notebook notebook = new Notebook(0, "Notebook", "", account, null, new HashSet<>());
        Notebook defaultNotebook = new Notebook(0, "All", "", account, null, new HashSet<>());

        when(notebookDaoMock.getDefaultNotebook(account)).thenReturn(defaultNotebook);
        noteService.setNotebookDao(notebookDaoMock);

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        when(noteDaoMock.saveNote(any(Note.class))).thenReturn(note);
        noteService.setNoteDao(noteDaoMock);

        NoteOutputForm actual = noteService.addNote(account, notebook, noteInputForm);
        verify(noteDaoMock).saveNote(noteArgumentCaptor.capture());
        Note capture = noteArgumentCaptor.getValue();
        Assert.assertEquals(capture.getTags().size(), 3);
        for (Tag tag : capture.getTags()) {
            Assert.assertTrue(tag.getName().equals("tag 1") ||
                    tag.getName().equals("tag 2") ||
                    tag.getName().equals("tag 3"));
        }
        for (Notebook captureNotebook : capture.getNotebooks()) {
            Assert.assertTrue(captureNotebook.getTitle().equals(defaultNotebook.getTitle()) ||
                              captureNotebook.getTitle().equals(notebook.getTitle()));
        }
    }

    @Test
    public void testSaveNote_brokenTags() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountService.getAccountDao().saveAccount(account);
        Notebook notebook = new Notebook(0, "Notebook", "", account, null, new HashSet<>());
        Notebook defaultNotebook = new Notebook(0, "All", "", account, null, new HashSet<>());

        when(notebookDaoMock.getDefaultNotebook(account)).thenReturn(defaultNotebook);
        noteService.setNotebookDao(notebookDaoMock);

        NoteInputForm noteInputForm = new NoteInputForm("Note 1", "Note 1 Text", "tag 1### #tag 2 #tag 3");
        Note note = new Note("Note 1", LocalDateTime.now(), "Note 1 Text", new HashSet<>());

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        when(noteDaoMock.saveNote(any(Note.class))).thenReturn(note);
        noteService.setNoteDao(noteDaoMock);

        NoteOutputForm actual = noteService.addNote(account, notebook, noteInputForm);
        verify(noteDaoMock).saveNote(noteArgumentCaptor.capture());
        Note capture = noteArgumentCaptor.getValue();
        Assert.assertEquals(capture.getTags().size(), 3);
        for (Tag tag : capture.getTags()) {
            Assert.assertTrue(tag.getName().equals("tag 1") ||
                    tag.getName().equals("tag 2") ||
                    tag.getName().equals("tag 3"));
        }
        for (Notebook captureNotebook : capture.getNotebooks()) {
            Assert.assertTrue(captureNotebook.getTitle().equals(defaultNotebook.getTitle()) ||
                    captureNotebook.getTitle().equals(notebook.getTitle()));
        }
    }

    @Test
    @Transactional
    public void testUpdateNote() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        Notebook notebook = new Notebook(0, "Notebook Q", "Q", account, null, new HashSet<>());
        Notebook defaultNotebook = new Notebook(0, "All", "", account, null, new HashSet<>());


        when(notebookDaoMock.getDefaultNotebook(account)).thenReturn(defaultNotebook);
        noteService.setNotebookDao(notebookDaoMock);

        accountService.getAccountDao().saveAccount(account);

        noteService.setNoteElasticDao(noteElasticDaoMock);

        NoteInputForm noteInputForm = new NoteInputForm("Note 1", "Note 1 Text", "#tag 1 #tag 2 #tag 3");
        NoteOutputForm noteOutputForm = noteService.addNote(account, notebook, noteInputForm);

        EditNoteInputForm editNoteInputForm = new EditNoteInputForm(0,"Edited Note 1", "", "#tag 1 #tag 3 #tag 4");
        Note note = new Note("Edited Note 1", LocalDateTime.now(), "Note 1 Text", new HashSet<>());
        note.setAccount(account);

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        when(noteDaoMock.updateNote(any(Note.class))).thenReturn(note);
        when(noteDaoMock.getNoteById(noteOutputForm.getId())).thenReturn(new Note(noteOutputForm.getId(),
                                                                                  account,
                                                                                  noteOutputForm.getName(),
                                                                                  LocalDateTime.now(),
                                                                                  null,
                                                                                  noteOutputForm.getTextAsString(),
                                                                                  null, null));

        noteService.setNoteDao(noteDaoMock);

        NoteOutputForm actual = noteService.updateNote(account, noteOutputForm.getId(), notebook, editNoteInputForm);
        verify(noteDaoMock).updateNote(noteArgumentCaptor.capture());
        Note capture = noteArgumentCaptor.getValue();
        Assert.assertEquals(note.getName(), capture.getName());
        Assert.assertEquals(note.getText(), capture.getText().trim());
        Assert.assertNotNull(capture.getModificationTime());
        Assert.assertEquals(capture.getTags().size(), 3);
        for (Tag tag : capture.getTags()) {
            Assert.assertTrue(tag.getName().equals("tag 1") ||
                    tag.getName().equals("tag 3") ||
                    tag.getName().equals("tag 4"));

        }
    }

    @Test
    @Transactional
    public void testGetAllNotesByAccount() {
        Note note1 = new Note("Note 1", LocalDateTime.now(), "Note 1 Text", new HashSet<>());
        Note note2 = new Note("Note 2", LocalDateTime.now(), "Note 2 Text", new HashSet<>());
        Note note3 = new Note("Note 3", LocalDateTime.now(), "Note 3 Text", new HashSet<>());

        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountService.getAccountDao().saveAccount(account);

        Set<NoteOutputForm> expected = new HashSet<>();
        expected.add(new NoteOutputForm(note1));
        expected.add(new NoteOutputForm(note2));
        expected.add(new NoteOutputForm(note3));

        when(noteDaoMock.findAllByAccountId(account.getId())).
                thenReturn(CollectionsUtils.convertArrayToSet(note1, note2, note3));
        noteService.setNoteDao(noteDaoMock);
        Set<NoteOutputForm> actual = noteService.getAllNotesByAccount(account);
        Assert.assertEquals(actual, expected);
    }
}