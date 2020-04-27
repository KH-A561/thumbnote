package net.thumbtack.thumbnote.thumbnoteroot.jpa.repository;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag save(Tag tag);

    Set<Tag> findByNameIn(Set<String> names);

    Set<Tag> findAllByAccount(Account account);

    void deleteAll();
}
