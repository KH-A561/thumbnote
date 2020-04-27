package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.integration.ControllerTestBase;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.TagDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.CollectionsUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NoteElasticDaoTest extends DaoTestBase {
    @Autowired
    private NoteDao noteDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private NoteElasticDao noteElasticDao;
    @Autowired
    private NotebookDao notebookDao;

    @Test
    @Transactional
    public void testFindAllByTagsInsideNotebook() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Account account2 = new Account(0, "Account Name", "Account2", "12345pass", "www@www.www");
        account2 = accountDao.saveAccount(account2);

        Notebook notebook1 = new Notebook(0, "Notebook Q", "Q", account1, null, new HashSet<>());
        Notebook notebook2 = new Notebook(0, "Notebook W", "W", account1, null, new HashSet<>());


        Tag tag1 = new Tag(0, "Tag Q", account1, new HashSet<>());
        Tag tag2 = new Tag(0, "Tag W", account1, new HashSet<>());
        Tag tag3 = new Tag(0, "Tag E", account1, new HashSet<>());
        Tag tag4 = new Tag(0, "Tag R", account1, new HashSet<>());
        Tag tag5 = new Tag(0, "Tag S", account1, new HashSet<>());

        Note note1;
        Note note2;
        Note note3;
        Note note4;
        Note note5;
        Note note6;
        Note note7;

        note1 = new Note(0, account1, "Qqq", LocalDateTime.now(), null,  "Qqqqqqqqqqqq",
                CollectionsUtils.convertArrayToSet(tag1, tag3, tag5), new HashSet<>());
        note2 = new Note(0, account2, "Www", LocalDateTime.now(), null,  "Wwwwwwwwwwwwww",
                CollectionsUtils.convertArrayToSet(tag3, tag4), new HashSet<>());
        note3 = new Note(0, account1, "Qqw", LocalDateTime.now(), null,  "Qqqqqqqqqqqw",
                CollectionsUtils.convertArrayToSet(tag2, tag4), new HashSet<>());
        note4 = new Note(0, account1, "Qqe", LocalDateTime.now(), null,  "Qqqqqqqqqqe",
                CollectionsUtils.convertArrayToSet(tag2, tag4, tag5), new HashSet<>());
        note5 = new Note(0, account2, "Wwe", LocalDateTime.now(), null,  "Wwwwwwwwwwwwwe",
                CollectionsUtils.convertArrayToSet(tag5), new HashSet<>());
        note6 = new Note(0, account1, "Qqr", LocalDateTime.now(), null,  "Qqqqqqqqqqqr",
                CollectionsUtils.convertArrayToSet(tag1, tag2, tag5), new HashSet<>());
        note7 = new Note(0, account2, "Wwr", LocalDateTime.now(), null,  "Wwwwwwwwwwwwr",
                CollectionsUtils.convertArrayToSet(tag3, tag5), new HashSet<>());

        notebook1.getNotes().add(note1);
        notebook2.getNotes().add(note1);
        note1.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note2);
        note2.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note3);
        notebook2.getNotes().add(note3);
        note3.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook2.getNotes().add(note4);
        note4.setNotebooks(CollectionsUtils.convertArrayToSet(notebook2));

        notebook1.getNotes().add(note5);
        note5.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note6);
        notebook2.getNotes().add(note6);
        note6.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note7);
        note7.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebookDao.saveNotebook(notebook1);
        notebookDao.saveNotebook(notebook2);

        noteElasticDao.saveNote(new ElasticNote(note1));
        noteElasticDao.saveNote(new ElasticNote(note2));
        noteElasticDao.saveNote(new ElasticNote(note3));
        noteElasticDao.saveNote(new ElasticNote(note4));
        noteElasticDao.saveNote(new ElasticNote(note5));
        noteElasticDao.saveNote(new ElasticNote(note6));
        noteElasticDao.saveNote(new ElasticNote(note7));


        Set<ElasticNote> expected = new HashSet<>();
        expected.add(new ElasticNote(note1));
        expected.add(new ElasticNote(note3));
        expected.add(new ElasticNote(note6));

        Set<ElasticNote> actual = noteElasticDao.findAllByTagsInsideNotebook(CollectionsUtils.convertArrayToSet("Tag Q", "Tag W"), notebook1, account1);

        Assert.assertEquals(actual, expected);
    }

    @Test
    @Transactional
    public void testFindAllByTextInsideNotebook() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Account account2 = new Account(0, "Account Name", "Account2", "12345pass", "www@www.www");
        account2 = accountDao.saveAccount(account2);

        Notebook notebook1 = new Notebook(0, "Notebook Q", "Q", account1, null, new HashSet<>());
        Notebook notebook2 = new Notebook(0, "Notebook W", "W", account1, null, new HashSet<>());


        Tag tag1 = new Tag(0, "Tag Q", account1, new HashSet<>());
        Tag tag2 = new Tag(0, "Tag W", account1, new HashSet<>());
        Tag tag3 = new Tag(0, "Tag E", account1, new HashSet<>());
        Tag tag4 = new Tag(0, "Tag R", account1, new HashSet<>());
        Tag tag5 = new Tag(0, "Tag S", account1, new HashSet<>());

        Note note1;
        Note note2;
        Note note3;
        Note note4;
        Note note5;
        Note note6;
        Note note7;

        note1 = new Note(0, account1, "Qqq", LocalDateTime.now(), null,  "Qwq qqq q qq qqqq",
                CollectionsUtils.convertArrayToSet(tag1, tag3, tag5), new HashSet<>());
        note2 = new Note(0, account2, "Www", LocalDateTime.now(), null,  "Wwwwwwwwwwwwww",
                CollectionsUtils.convertArrayToSet(tag3, tag4), new HashSet<>());
        note3 = new Note(0, account1, "Qqw", LocalDateTime.now(), null,  "Qqqqqq www qqw",
                CollectionsUtils.convertArrayToSet(tag2, tag4), new HashSet<>());
        note4 = new Note(0, account1, "Qqe", LocalDateTime.now(), null,  "Qqqqqq qqqqe",
                CollectionsUtils.convertArrayToSet(tag2, tag4, tag5), new HashSet<>());
        note5 = new Note(0, account2, "Wwe", LocalDateTime.now(), null,  "Wwwwwwwwwww q wwe",
                CollectionsUtils.convertArrayToSet(tag5), new HashSet<>());
        note6 = new Note(0, account1, "Qqr", LocalDateTime.now(), null,  "Qqqqqqqqq eee",
                CollectionsUtils.convertArrayToSet(tag1, tag2, tag5), new HashSet<>());
        note7 = new Note(0, account2, "Wwr", LocalDateTime.now(), null,  "Wwwwwww wwwwwr",
                CollectionsUtils.convertArrayToSet(tag3, tag5), new HashSet<>());

        notebook1.getNotes().add(note1);
        notebook2.getNotes().add(note1);
        note1.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note2);
        note2.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note3);
        notebook2.getNotes().add(note3);
        note3.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook2.getNotes().add(note4);
        note4.setNotebooks(CollectionsUtils.convertArrayToSet(notebook2));

        notebook1.getNotes().add(note5);
        note5.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note6);
        notebook2.getNotes().add(note6);
        note6.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note7);
        note7.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebookDao.saveNotebook(notebook1);
        notebookDao.saveNotebook(notebook2);

        noteElasticDao.saveNote(new ElasticNote(note1));
        noteElasticDao.saveNote(new ElasticNote(note2));
        noteElasticDao.saveNote(new ElasticNote(note3));
        noteElasticDao.saveNote(new ElasticNote(note4));
        noteElasticDao.saveNote(new ElasticNote(note5));
        noteElasticDao.saveNote(new ElasticNote(note6));
        noteElasticDao.saveNote(new ElasticNote(note7));


        Set<ElasticNote> expected = new HashSet<>();
        expected.add(new ElasticNote(note1));
        expected.add(new ElasticNote(note3));
        expected.add(new ElasticNote(note6));

        Set<ElasticNote> actual = noteElasticDao.findAllByTextInsideNotebook("Qqq Www Eee", notebook1, account1);

        Assert.assertEquals(actual, expected);
    }

    @Test
    @Transactional
    public void testFindAllByNameInsideNotebook() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Account account2 = new Account(0, "Account Name", "Account2", "12345pass", "www@www.www");
        account2 = accountDao.saveAccount(account2);

        Notebook notebook1 = new Notebook(0, "Notebook Q", "Q", account1, null, new HashSet<>());
        Notebook notebook2 = new Notebook(0, "Notebook W", "W", account1, null, new HashSet<>());


        Tag tag1 = new Tag(0, "Tag Q", account1, new HashSet<>());
        Tag tag2 = new Tag(0, "Tag W", account1, new HashSet<>());
        Tag tag3 = new Tag(0, "Tag E", account1, new HashSet<>());
        Tag tag4 = new Tag(0, "Tag R", account1, new HashSet<>());
        Tag tag5 = new Tag(0, "Tag S", account1, new HashSet<>());

        Note note1;
        Note note2;
        Note note3;
        Note note4;
        Note note5;
        Note note6;
        Note note7;

        note1 = new Note(0, account1, "Qqq qwe www", LocalDateTime.now(), null,  "Qqqqqqqqqqqq",
                CollectionsUtils.convertArrayToSet(tag1, tag3, tag5), new HashSet<>());
        note2 = new Note(0, account2, "Www q w e sss", LocalDateTime.now(), null,  "Wwwwwwwwwwwwww",
                CollectionsUtils.convertArrayToSet(tag3, tag4), new HashSet<>());
        note3 = new Note(0, account1, "Qqw rty sss ", LocalDateTime.now(), null,  "Qqqqqqqqqqqw",
                CollectionsUtils.convertArrayToSet(tag2, tag4), new HashSet<>());
        note4 = new Note(0, account1, "Qwe Rty", LocalDateTime.now(), null,  "Qqqqqqqqqqe",
                CollectionsUtils.convertArrayToSet(tag2, tag4, tag5), new HashSet<>());
        note5 = new Note(0, account2, "Wweqwe", LocalDateTime.now(), null,  "Wwwwwwwwwwwwwe",
                CollectionsUtils.convertArrayToSet(tag5), new HashSet<>());
        note6 = new Note(0, account1, "Qwe Asd Rty", LocalDateTime.now(), null,  "Qqqqqqqqqqqr",
                CollectionsUtils.convertArrayToSet(tag1, tag2, tag5), new HashSet<>());
        note7 = new Note(0, account2, "Wwrffff", LocalDateTime.now(), null,  "Wwwwwwwwwwwwr",
                CollectionsUtils.convertArrayToSet(tag3, tag5), new HashSet<>());

        notebook1.getNotes().add(note1);
        notebook2.getNotes().add(note1);
        note1.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note2);
        note2.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note3);
        notebook2.getNotes().add(note3);
        note3.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook2.getNotes().add(note4);
        note4.setNotebooks(CollectionsUtils.convertArrayToSet(notebook2));

        notebook1.getNotes().add(note5);
        note5.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebook1.getNotes().add(note6);
        notebook2.getNotes().add(note6);
        note6.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1, notebook2));

        notebook1.getNotes().add(note7);
        note7.setNotebooks(CollectionsUtils.convertArrayToSet(notebook1));

        notebookDao.saveNotebook(notebook1);
        notebookDao.saveNotebook(notebook2);

        noteElasticDao.saveNote(new ElasticNote(note1));
        noteElasticDao.saveNote(new ElasticNote(note2));
        noteElasticDao.saveNote(new ElasticNote(note3));
        noteElasticDao.saveNote(new ElasticNote(note4));
        noteElasticDao.saveNote(new ElasticNote(note5));
        noteElasticDao.saveNote(new ElasticNote(note6));
        noteElasticDao.saveNote(new ElasticNote(note7));


        Set<ElasticNote> expected = new HashSet<>();
        expected.add(new ElasticNote(note1));
        expected.add(new ElasticNote(note3));
        expected.add(new ElasticNote(note6));

        Set<ElasticNote> actual = noteElasticDao.findAllByNameInsideNotebook("Qwe Rty", notebook1, account1);

        Assert.assertEquals(actual, expected);
    }
}
