package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.integration.ControllerTestBase;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.AccountDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.TagDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteDaoTest extends DaoTestBase {
    @Autowired
    private NoteDao noteDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private AccountDao accountDao;

    @Test
    public void testSaveNote() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);

        Set<Tag> tags = new HashSet<>();
        Note note = new Note(0, account, "Note 1", LocalDateTime.now(), null,  "Note 1 Text",
                             tags, new HashSet<>());
        tags.add(new Tag("Tag 3", account, Collections.singleton(note)));
        tags.add(new Tag("Tag 2", account, Collections.singleton(note)));
        tags.add(new Tag("Tag 1", account, Collections.singleton(note)));

        Note actual = noteDao.saveNote(note);

        Assert.assertNotEquals(actual.getId(), 0);
        Assert.assertEquals(actual.getTags().size(), 3);
        for (Tag tag : actual.getTags()) {
            Assert.assertNotEquals(tag.getId(), 0);
        }
        Assert.assertEquals(note.getAccount(), account);
    }

    @Test
    public void testFindAllNotes() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account = accountDao.saveAccount(account);

        Note note1 = new Note(0, account, "Note 1", LocalDateTime.now(), null,  "Note 1 Text",
                              new HashSet<>(), new HashSet<>());
        Note note2 = new Note(0, account, "Note 2", LocalDateTime.now(), null,  "Note 2 Text",
                new HashSet<>(), new HashSet<>());
        Note note3 = new Note(0, account, "Note 3", LocalDateTime.now(), null,  "Note 3 Text",
                new HashSet<>(), new HashSet<>());

        Set<Note> expected = new HashSet<>();
        expected.add(noteDao.saveNote(note1));
        expected.add(noteDao.saveNote(note2));
        expected.add(noteDao.saveNote(note3));
        Set<Note> actual = noteDao.findAll();

        for (Note note : actual) {
            Assert.assertEquals(note.getAccount(), account);
            Assert.assertTrue(expected.contains(note));
        }
        Assert.assertEquals(actual.size(), expected.size());
    }

    @Test
    @Transactional
    public void testFindAllNotesByAccountId() {
        Account account1 = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        account1 = accountDao.saveAccount(account1);

        Account account2 = new Account(0, "Account Name", "Account2", "12345pass", "www@www.www");
        account2 = accountDao.saveAccount(account2);

        Note note1 = new Note(0, account1, "Note 1", LocalDateTime.now(), null,  "Note 1 Text",
                new HashSet<>(), new HashSet<>());
        Note note2 = new Note(0, account2, "Note 2", LocalDateTime.now(), null,  "Note 2 Text",
                new HashSet<>(), new HashSet<>());
        Note note3 = new Note(0, account1, "Note 3", LocalDateTime.now(), null,  "Note 3 Text",
                new HashSet<>(), new HashSet<>());

        Set<Note> expected1 = new HashSet<>();
        Set<Note> expected2 = new HashSet<>();
        expected1.add(noteDao.saveNote(note1));
        expected2.add(noteDao.saveNote(note2));
        expected1.add(noteDao.saveNote(note3));

        Set<Note> actual1 = noteDao.findAllByAccountId(account1.getId());
        Set<Note> actual2 = noteDao.findAllByAccountId(account2.getId());

        Assert.assertEquals(actual1, expected1);
        Assert.assertEquals(actual2, expected2);
    }

    @Test
    public void testDeleteNote() {
        Account account = new Account(0, "Account Name", "Account1", "12345pass", "qqq@qqq.qqq");
        accountDao.saveAccount(account);

        Note note1 = new Note(0, account, "Note 1", LocalDateTime.now(), null,  "Note 1 Text",
                new HashSet<>(), new HashSet<>());
        Note note2 = new Note(0, account, "Note 2", LocalDateTime.now(), null,  "Note 2 Text",
                new HashSet<>(), new HashSet<>());
        Note note3 = new Note(0, account, "Note 3", LocalDateTime.now(), null,  "Note 3 Text",
                new HashSet<>(), new HashSet<>());

        Set<Note> expected = new HashSet<>();
        noteDao.saveNote(note1);
        expected.add(noteDao.saveNote(note2));
        expected.add(noteDao.saveNote(note3));
        noteDao.deleteById(note1.getId());
        Set<Note> actual = noteDao.findAll();

        Assert.assertEquals(actual, expected);
    }



}
