package net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl;

import net.thumbtack.thumbnote.thumbnoteroot.jpa.dao.TagDao;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Tag;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.TagInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.TagOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    public TagOutputForm addTag(TagInputForm tagInputForm) {
        Tag tag = new Tag(tagInputForm.getName());

        return null;
    }

    @Override
    public Set<TagOutputForm> getAllTagsByAccount(Account account) {
        Set<Tag> tags = tagDao.findAllTagsByAccount(account);
        return tags.stream().map(TagOutputForm::new).collect(Collectors.toSet());
    }

    @Override
    public TagOutputForm getById(Account account, Integer id) throws IllegalAccessException {
        Optional<Tag> optionalTag;
        if (!(optionalTag = tagDao.findTagById(id)).isPresent()) {
            throw new IllegalArgumentException("tag not found");
        }
        Tag tag = optionalTag.get();
        if (!tag.getAccount().equals(account)) {
            throw new IllegalAccessException();
        }
        return new TagOutputForm(tag);
    }

}
