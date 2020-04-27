package net.thumbtack.thumbnote.thumbnoteroot.spring.controller;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SearchInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.TagInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.TagOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.TagService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.serviceImpl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{id}")
    @Transactional
    public String getTagWithNotes(Model model, @PathVariable("id") Integer id) throws IllegalAccessException {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", account);
        TagOutputForm tagOutputForm = tagService.getById(account, id);
        model.addAttribute("currentTag", tagOutputForm);
        model.addAttribute("notes", tagOutputForm.getNotes());
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "notes";
    }
}
