package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NotebookInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotebookServiceImpl implements NotebookService {
    @Autowired
    private NotebookDao notebookDao;
    @Autowired
    private NoteDao noteDao;

    @Override
    public Set<NotebookOutputForm> getAllNotebooksByAccount(Account account) {
        Set<Notebook> notebooks = notebookDao.findAllByAccount(account);
        return notebooks.stream().map(NotebookOutputForm::new).collect(Collectors.toSet());
    }

    @Override
    public NotebookOutputForm addNotebook(Account account, NotebookInputForm notebookInputForm) {
        Set<Note> notes = noteDao.findAllByNames(new HashSet<>(notebookInputForm.getNoteNames()),
                                                                       account);
        Notebook notebook = new Notebook(account, notebookInputForm, notes);
        notebookDao.saveNotebook(notebook);
        return new NotebookOutputForm(notebook);
    }

    @Override
    public NotebookOutputForm getById(Integer id) {
        Notebook notebook = notebookDao.getById(id);
        return new NotebookOutputForm(notebook);
    }

    @Override
    public NotebookOutputForm updateNotebook(Account account, Notebook notebook, NotebookInputForm editedNotebook) {
        Set<Note> notes = noteDao.findAllByNames(new HashSet<>(editedNotebook.getNoteNames()), account);
        Notebook newNotebook = new Notebook(account, editedNotebook, notes);
        newNotebook.setId(notebook.getId());
        newNotebook.setModificationTime(LocalDateTime.now());
        return new NotebookOutputForm(notebookDao.updateNotebook(newNotebook));
    }

    @Override
    public void deleteNotebook(Account account, Notebook notebook) {
        notebookDao.deleteNotebook(notebook);
    }
}
