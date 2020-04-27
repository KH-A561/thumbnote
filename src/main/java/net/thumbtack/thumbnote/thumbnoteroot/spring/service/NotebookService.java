package net.thumbtack.thumbnote.thumbnoteroot.spring.service;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NotebookInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;

import java.util.Set;

public interface NotebookService {
    Set<NotebookOutputForm> getAllNotebooksByAccount(Account account);

    NotebookOutputForm addNotebook(Account account, NotebookInputForm notebookInputForm);

    NotebookOutputForm getById(Integer id);

    NotebookOutputForm updateNotebook(Account account, Notebook notebook, NotebookInputForm editedNotebook);

    void deleteNotebook(Account account, Notebook notebook);
}
