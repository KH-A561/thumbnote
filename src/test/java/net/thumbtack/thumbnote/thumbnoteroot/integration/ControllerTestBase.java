package net.thumbtack.thumbnote.thumbnoteroot.integration;

import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.AccountRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NoteRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NotebookRepository;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.TagRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public abstract class ControllerTestBase {
    @Autowired
    private WebApplicationContext webApplicationContext;

    protected RestTemplate template = new RestTemplate();
    protected MockMvc mockMvc;

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

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
