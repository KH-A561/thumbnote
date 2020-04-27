package net.thumbtack.thumbnote.thumbnoteroot.spring.service;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.TagInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.TagOutputForm;

import java.util.Set;

public interface TagService {
    Set<TagOutputForm> getAllTagsByAccount(Account account);

    TagOutputForm getById(Account account, Integer id) throws IllegalAccessException;
}
