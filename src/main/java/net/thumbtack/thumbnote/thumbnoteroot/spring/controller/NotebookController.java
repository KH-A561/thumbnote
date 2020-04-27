package net.thumbtack.thumbnote.thumbnoteroot.spring.controller;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Note;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.EditNoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NotebookInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SearchInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notebook")
public class NotebookController {
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private NoteService noteService;

    @Value("${defaultNotebookName}")
    private String defaultNotebookName;

    @PostMapping(value = "/create")
    @Transactional
    public String addNotebook(Model model,
                              @ModelAttribute("notebookInputForm") @Valid NotebookInputForm notebookInputForm,
                              BindingResult result,
                              Errors errors) {
        Account account = accountService.getCurrentAccount();
        if (notebookInputForm.getTitle().equals(defaultNotebookName)) {
            errors.rejectValue("title", "", "Please don't use default notebook's title");
        }
        if (result.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("notes", noteService.getAllNotesByAccount(account));
            model.addAttribute("notebookInputForm", new NotebookInputForm());
            model.addAttribute("errors", errors);
            return "addNotebook";
        }
        NotebookOutputForm notebook = notebookService.addNotebook(account, notebookInputForm);
        return String.format("redirect:/notebook/%s", notebook.getId());
    }

    @GetMapping(value = "/create")
    @Transactional
    public String showAddNotebookPage(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("account", account);
        model.addAttribute("notes", noteService.getAllNotesByAccount(account));
        model.addAttribute("notebookInputForm", new NotebookInputForm());
        return "addNotebook";
    }

    @GetMapping(path = "/{id}")
    @Transactional
    public String getNotebook(Model model, @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(id);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebookService.getById(id));
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "notes";
    }

    @GetMapping(path = "/{id}/edit")
    @Transactional
    public String showEditNotebookPage(Model model, @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(id);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notes", noteService.getAllNotesByAccount(account));
        model.addAttribute("notebook", notebook);
        model.addAttribute("editNotebookInputForm", new NotebookInputForm(notebook.getTitle(),
                                                                             notebook.getDescription(),
                                                                             notebook.getNotes().stream().
                                                                                      map(Note::getName).collect(Collectors.toList())));
        return "editNotebook";
    }

    @PostMapping(path = "/{id}/edit")
    @Transactional
    public String updateNotebook(Model model,
                                 @ModelAttribute("editNotebookInputForm") @Valid NotebookInputForm editedNotebook,
                                 BindingResult result,
                                 Errors errors,
                                 @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(id);
        if (editedNotebook.getTitle().equals(defaultNotebookName)) {
            errors.rejectValue("title", "", "Please don't use default notebook's title");
        }
        if (result.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("notes", noteService.getAllNotesByAccount(account));
            model.addAttribute("notebookInputForm", new NotebookInputForm());
            model.addAttribute("errors", errors);
            return "addNotebook";
        }
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        notebookService.updateNotebook(account, new Notebook(notebook), editedNotebook);
        return "redirect:/account/notebooks";
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public String deleteNotebook(Model model,
                                 @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(id);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        notebookService.deleteNotebook(account, new Notebook(notebook));
        return "redirect:/account/notebooks";
    }
}
