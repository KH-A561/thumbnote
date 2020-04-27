package net.thumbtack.thumbnote.thumbnoteroot.jpa.repository;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Repository
public interface NoteRepository extends Repository<Note, Integer> {
    Note save(Note note);

    Note saveAndFlush(Note note);

    Optional<Note> findById(Integer id);

    void delete(Note note);

    long count();

    void deleteAll();

    Set<Note> findAll();

    Set<Note> findAllByIdIn(Set<Integer> ids);

    void deleteById(Integer id);

    @Query("SELECT n FROM Note n WHERE n.account.id = :accountId")
    Set<Note> findAllByAccountId(@Param("accountId") Integer accountId);

    Set<Note> findAllByNameInAndAccount(HashSet<String> names, Account account);
}
