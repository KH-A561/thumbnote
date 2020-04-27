package net.thumbtack.thumbnote.thumbnoteroot.dao;

import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.AccountRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NoteRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NotebookRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.TagRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class DaoTestBase {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private NotebookRepository notebookRepository;


    @Before
    public void clearDatabase() {
        notebookRepository.deleteAll();
        noteRepository.deleteAll();
        tagRepository.deleteAll();
        accountRepository.deleteAll();
    }
}
