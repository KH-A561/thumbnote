package net.thumbtack.thumbnote.thumbnoteroot.spring.controller;

import lombok.var;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.LogInAccountInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NotebookInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SearchInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SignUpAccountInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NotebookService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.SecurityService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.TagService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.validator.SignUpAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private SignUpAccountValidator accountValidator;
    @Autowired
    private NotebookService notebookService;

    @Value("${defaultNotebookName}")
    String defaultNotebookName;


    @GetMapping(value = "/signup")
    public String showRegistrationPage(Model model) {
        SignUpAccountInputForm signUpAccountInputForm = new SignUpAccountInputForm();
        model.addAttribute("signUpAccountInputForm", signUpAccountInputForm);
        return "signUp";
    }

    @PostMapping(value = "/signup")
    @Transactional
    public String signUpAccount(@ModelAttribute("signUpAccountInputForm") @Valid
                                        SignUpAccountInputForm signUpAccountInputForm,
                                BindingResult result,
                                Errors errors,
                                Model model) {
        accountValidator.validate(signUpAccountInputForm, errors);
        if (result.hasErrors()) {
            model.addAttribute("signUpAccountInputForm", signUpAccountInputForm);
            model.addAttribute("errors", errors.getAllErrors());
            return "signUp";
        }
        Account registered = accountService.signUpAccount(signUpAccountInputForm, result);
        securityService.autoLogin(registered.getLogin(), signUpAccountInputForm.getPassword());
        var notebook = notebookService.addNotebook(registered, new NotebookInputForm(defaultNotebookName, "This notebook contains all your notes by default", new ArrayList<>()));
        model.addAttribute("account", registered);
        return "redirect:/account/notebooks";
    }

    @GetMapping(value = "/login")
    public String showLogInPage(@RequestParam(name = "error", required = false) Boolean error,
                                Model model) {
        if (Boolean.TRUE.equals(error)) {
            model.addAttribute("error", true);
        }
        LogInAccountInputForm logInAccountInputForm = new LogInAccountInputForm();
        model.addAttribute("logInAccountInputForm", logInAccountInputForm);
        return "login";
    }

    @GetMapping(value = "/notes")
    public String showAccountNotes(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", account);
        model.addAttribute("notes", noteService.getAllNotesByAccount(account));
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "notes";
    }

    @GetMapping(value = "/tags")
    public String showAccountTags(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", account);
        model.addAttribute("tags", tagService.getAllTagsByAccount(account));
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "tags";
    }

    @GetMapping(value = "/notebooks")
    @Transactional
    public String showAccountNotebooks(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", account);
        var notebooks = notebookService.getAllNotebooksByAccount(account);
        if (notebooks.isEmpty()) {
            var defaultNotebook = notebookService.addNotebook(account, new NotebookInputForm(defaultNotebookName,
                                                    "This notebook contains all your notes by default",
                                                              noteService.getAllNotesByAccount(account).stream().
                                                                          map(NoteOutputForm::getName).
                                                                          collect(Collectors.toList())));
            notebooks.add(defaultNotebook);
        }
        model.addAttribute("defaultNotebookName", defaultNotebookName);
        model.addAttribute("notebooks", notebooks);
        return "notebooks";
    }
}
