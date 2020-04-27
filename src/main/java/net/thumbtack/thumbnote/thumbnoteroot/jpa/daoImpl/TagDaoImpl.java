package net.thumbtack.thumbnote.thumbnoteroot.jpa.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.TagDao;
import net.thumbtack.thumbnote.thumbnoteroot.jpa.repository.TagRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class TagDaoImpl implements TagDao {
    private final TagRepository tagRepository;

    @Autowired
    public TagDaoImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> findTagsByNames(Set<String> tagNames) {
        try {
            return tagRepository.findByNameIn(tagNames);
        } catch (DataAccessException e) {
            log.error("Can't find tags {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<Tag> findAllTagsByAccount(Account account) {
        try {
            return tagRepository.findAllByAccount(account);
        } catch (DataAccessException e) {
            log.error("Can't find tags {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<Tag> findTagById(Integer id) {
        try {
            return tagRepository.findById(id);
        } catch (DataAccessException e) {
            log.error("Can't find tags {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }


}
