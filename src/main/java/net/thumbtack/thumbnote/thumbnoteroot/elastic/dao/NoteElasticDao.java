package net.thumbtack.thumbnote.thumbnoteroot.elastic.dao;

import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;

import java.util.Set;

public interface NoteElasticDao {
    void saveNote(ElasticNote note);
    void deleteById(Integer id);
    Set<ElasticNote> findAllByTagsInsideNotebook(Set<String> tags, Notebook notebook, Account account);
    Set<ElasticNote> findAllByNameInsideNotebook(String name, Notebook notebook, Account account);
    Set<ElasticNote> findAllByTextInsideNotebook(String searchInput, Notebook notebook, Account account);
}
