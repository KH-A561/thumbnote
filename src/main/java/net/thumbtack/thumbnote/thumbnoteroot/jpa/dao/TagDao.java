package net.thumbtack.thumbnote.thumbnoteroot.jpa.dao;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagDao {
    Set<Tag> findTagsByNames(Set<String> tagNames);
    Set<Tag> findAllTagsByAccount(Account account);
    Optional<Tag> findTagById(Integer id);
}
