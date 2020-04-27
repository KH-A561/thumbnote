package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagDaoTest extends DaoTestBase {
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
    public void testFindTagsByNames() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Notebook notebook1 = new Notebook(0, "Notebook Q", "Q", account1, null, new HashSet<>());

        Tag tag1 = new Tag(0, "Tag Q", account1, new HashSet<>());
        Tag tag2 = new Tag(0, "Tag W", account1, new HashSet<>());
        Tag tag3 = new Tag(0, "Tag E", account1, new HashSet<>());
        Tag tag4 = new Tag(0, "Tag R", account1, new HashSet<>());
        Tag tag5 = new Tag(0, "Tag S", account1, new HashSet<>());

        List<Note> notes = new ArrayList<>();
        notes.add(new Note(0, account1, "Qqq", LocalDateTime.now(), null,  "Qqqqqqqqqqqq",
                CollectionsUtils.convertArrayToSet(tag1, tag3, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Www", LocalDateTime.now(), null,  "Wwwwwwwwwwwwww",
                CollectionsUtils.convertArrayToSet(tag3, tag4), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqw", LocalDateTime.now(), null,  "Qqqqqqqqqqqw",
                CollectionsUtils.convertArrayToSet(tag2, tag4), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqe", LocalDateTime.now(), null,  "Qqqqqqqqqqe",
                CollectionsUtils.convertArrayToSet(tag2, tag4, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Wwe", LocalDateTime.now(), null,  "Wwwwwwwwwwwwwe",
                CollectionsUtils.convertArrayToSet(tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqr", LocalDateTime.now(), null,  "Qqqqqqqqqqqr",
                CollectionsUtils.convertArrayToSet(tag1, tag2, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Wwr", LocalDateTime.now(), null,  "Wwwwwwwwwwwwr",
                CollectionsUtils.convertArrayToSet(tag3, tag5), new HashSet<>()));

        notebook1.getNotes().addAll(notes);
        notebookDao.saveNotebook(notebook1);

        Set<Tag> actual = tagDao.findTagsByNames(CollectionsUtils.convertArrayToSet("Tag Q", "Tag E", "Tag S"));
        for (Tag tag : actual) {
            Assert.assertTrue(tag.getName().equals("Tag Q") ||
                    tag.getName().equals("Tag E") ||
                    tag.getName().equals("Tag S"));
        }
    }

    @Test
    @Transactional
    public void testFindTagsByAccount() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Notebook notebook1 = new Notebook(0, "Notebook Q", "Q", account1, null, new HashSet<>());

        Tag tag1 = new Tag(0, "Tag Q", account1, new HashSet<>());
        Tag tag2 = new Tag(0, "Tag W", account1, new HashSet<>());
        Tag tag3 = new Tag(0, "Tag E", account1, new HashSet<>());
        Tag tag4 = new Tag(0, "Tag R", account1, new HashSet<>());
        Tag tag5 = new Tag(0, "Tag S", account1, new HashSet<>());

        List<Note> notes = new ArrayList<>();
        notes.add(new Note(0, account1, "Qqq", LocalDateTime.now(), null,  "Qqqqqqqqqqqq",
                CollectionsUtils.convertArrayToSet(tag1, tag3, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Www", LocalDateTime.now(), null,  "Wwwwwwwwwwwwww",
                CollectionsUtils.convertArrayToSet(tag3, tag4), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqw", LocalDateTime.now(), null,  "Qqqqqqqqqqqw",
                CollectionsUtils.convertArrayToSet(tag2, tag4), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqe", LocalDateTime.now(), null,  "Qqqqqqqqqqe",
                CollectionsUtils.convertArrayToSet(tag2, tag4, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Wwe", LocalDateTime.now(), null,  "Wwwwwwwwwwwwwe",
                CollectionsUtils.convertArrayToSet(tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Qqr", LocalDateTime.now(), null,  "Qqqqqqqqqqqr",
                CollectionsUtils.convertArrayToSet(tag1, tag2, tag5), new HashSet<>()));
        notes.add(new Note(0, account1, "Wwr", LocalDateTime.now(), null,  "Wwwwwwwwwwwwr",
                CollectionsUtils.convertArrayToSet(tag3, tag5), new HashSet<>()));

        notebook1.getNotes().addAll(notes);
        notebookDao.saveNotebook(notebook1);

        Set<Tag> actual = tagDao.findAllTagsByAccount(account1);
        for (Tag tag : actual) {
            Assert.assertTrue(tag.getName().equals("Tag Q") ||
                    tag.getName().equals("Tag W") ||
                    tag.getName().equals("Tag E") ||
                    tag.getName().equals("Tag R") ||
                    tag.getName().equals("Tag S"));
        }
    }
}
