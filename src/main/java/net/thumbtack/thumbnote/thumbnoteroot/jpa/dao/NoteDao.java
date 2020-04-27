package net.thumbtack.thumbnote.thumbnoteroot.jpa.dao;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface NoteDao {
    Note saveNote(Note note);
    Note getNoteById(int id);
    Note updateNote(Note note);
    Set<Note> findAll();
    void deleteById(Integer id);
    Set<Note> findAllByAccountId(Integer accountId);
    Set<Note> findAllByIdIn(Set<Integer> ids);
    Set<Note> findAllByNames(HashSet<String> names, Account account);
}
