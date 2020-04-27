package net.thumbtack.thumbnote.thumbnoteroot.spring.service;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.EditNoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;

import java.util.Set;


public interface NoteService {
    NoteOutputForm addNote(Account account, Notebook notebook, NoteInputForm noteInputForm);
    NoteOutputForm updateNote(Account account, Integer id, Notebook notebook, EditNoteInputForm noteInputForm);
    void deleteNote(Account account, Notebook notebook, Integer id);
    Set<NoteOutputForm> getAllNotesByAccount(Account account);
    Set<NoteOutputForm> getAllNotesFromNotebookByName(Account account, Notebook notebook, String searchInput);
    Set<NoteOutputForm> getAllNotesFromNotebookByTags(Account account, Notebook notebook, Set<String> tags);
    Set<NoteOutputForm> getAllNotesFromNotebookByText(Account account, Notebook notebook, String searchInput);
    void moveNoteToNotebook(Account account, Integer noteId, Notebook fromNotebook, Notebook toNotebook);
}
