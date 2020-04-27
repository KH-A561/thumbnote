package net.thumbtack.thumbnote.thumbnoteroot.jpa.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NoteDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NoteRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class NoteDaoImpl implements NoteDao {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteDaoImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note saveNote(Note note) {
        try {
            log.debug("Saving note {} for account {}", note.getName(), note.getAccount().getLogin());
            return noteRepository.save(note);
        } catch (DataAccessException e) {
            log.error("Can't insert note {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Note getNoteById(int id) {
        try {
            Optional<Note> noteOptional;
            if ((noteOptional = noteRepository.findById(id)).isPresent()) {
                return noteOptional.get();
            } else {
                //TODO: ServerException type
                throw new IllegalArgumentException();
            }
        } catch (DataAccessException e) {
            log.error("Can't get note by id {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Note updateNote(Note note) {
        try {
            return noteRepository.saveAndFlush(note);
        } catch (DataAccessException e) {
            log.error("Can't update note {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<Note> findAll() {
        try {
            return noteRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Can't get notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            noteRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Can't delete note {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<Note> findAllByAccountId(Integer accountId) {
        try {
            return noteRepository.findAllByAccountId(accountId);
        } catch (DataAccessException e) {
            log.error("Can't get notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<Note> findAllByIdIn(Set<Integer> ids) {
        try {
            return noteRepository.findAllByIdIn(ids);
        } catch (DataAccessException e) {
            log.error("Can't get notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<Note> findAllByNames(HashSet<String> names, Account account) {
        try {
            return noteRepository.findAllByNameInAndAccount(names, account);
        } catch (DataAccessException e) {
            log.error("Can't get notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }
}
