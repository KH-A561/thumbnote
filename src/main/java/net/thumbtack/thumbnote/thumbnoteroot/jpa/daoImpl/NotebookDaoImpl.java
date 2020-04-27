package net.thumbtack.thumbnote.thumbnoteroot.jpa.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.NotebookDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.NotebookRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotebookDaoImpl implements NotebookDao {
    @Autowired
    private NotebookRepository notebookRepository;

    @Value("${defaultNotebookName}")
    String defaultNotebookName;

    @Override
    public Set<Notebook> findAllByAccount(Account account) {
        try {
            return notebookRepository.findAllByAccount(account);
        } catch (DataAccessException e) {
            log.error("Can't get notebooks {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Notebook saveNotebook(Notebook notebook) {
        try {
            return notebookRepository.save(notebook);
        } catch (DataAccessException e) {
            log.error("Can't get notebooks {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Notebook getById(Integer id) {
        try {
            Notebook notebook = notebookRepository.getById(id);
            if (notebook == null) {
                throw new IllegalArgumentException("Notebook null");
            } else {
                return notebook;
            }
        } catch (DataAccessException e) {
            log.error("Can't get notebooks {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Notebook getDefaultNotebook(Account account) {
        try {
            Set<Notebook> notebooks = notebookRepository.getByTitleAndAccount(defaultNotebookName, account);
            Notebook notebook;
            if (notebooks.size() > 1) {
                List<Notebook> notebookList = notebooks.stream().sorted(Comparator.comparing(Notebook::getModificationTime)).collect(Collectors.toCollection(LinkedList::new));
                notebook = notebookList.get(0);
            } else {
                notebook = notebooks.iterator().next();
            }
            return notebook;
        } catch (DataAccessException e) {
            log.error("Can't get notebooks {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Notebook updateNotebook(Notebook notebook) {
        try {
            return notebookRepository.saveAndFlush(notebook);
        } catch (DataAccessException e) {
            log.error("Can't update notebooks {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteNotebook(Notebook notebook) {
        try {
            notebookRepository.delete(notebook);
        } catch (DataAccessException e) {
            log.error("Can't delete notebook {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }


}
