package net.thumbtack.thumbnote.thumbnoteroot.spring.controller;

import lombok.var;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.EditNoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.SearchInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.TagFormUtils;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.TagUtils;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notebook/{notebookId}/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/create")
    @Transactional
    public String addNote(Model model,
                          @ModelAttribute("noteInputForm") @Valid NoteInputForm noteInputForm,
                          BindingResult result,
                          Errors errors,
                          @PathVariable("notebookId") Integer notebookId) {
        //TODO:VALIDATION
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        NoteOutputForm currentNote = noteService.addNote(account, new Notebook(notebook), noteInputForm);
        model.addAttribute("currentNote", currentNote);
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("searchInputForm", new SearchInputForm());
        model.addAttribute("notebooks", notebookService.getAllNotebooksByAccount(account));
        return String.format("redirect:/notebook/%s/note/%s", notebookId, currentNote.getId());
    }

    @GetMapping(value = "/create")
    public String showAddNotePage(Model model,
                                  @PathVariable("notebookId") Integer notebookId) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        NoteInputForm noteInputForm = new NoteInputForm();
        model.addAttribute("noteInputForm", noteInputForm);
        model.addAttribute("notebook", notebook);
        model.addAttribute("account", account);
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "addNote";
    }

    @GetMapping(path = "/{id}")
    @Transactional
    public String getNote(Model model,
                          @PathVariable("notebookId") Integer notebookId,
                          @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        Optional<NoteOutputForm> noteOutputForm;
        if (!((noteOutputForm = notebook.getNotesAsForms().stream().
                filter(n -> n.getId().equals(id)).findFirst()).isPresent())) {
            throw new IllegalArgumentException("Note not found");
        }
        model.addAttribute("currentNote", noteOutputForm.get());
        model.addAttribute("notebooks", notebookService.getAllNotebooksByAccount(account));
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "notes";
    }

    @GetMapping(path = "/{id}/edit")
    @Transactional
    public String showEditNotePage(Model model,
                                   @PathVariable("notebookId") Integer notebookId,
                                   @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        Optional<NoteOutputForm> noteOutputForm;
        if (!(noteOutputForm = notebook.getNotesAsForms().stream().
                filter(n -> n.getId().equals(id)).findFirst()).isPresent()) {
            throw new IllegalArgumentException("Note not found");
        }
        model.addAttribute("currentNote", new EditNoteInputForm(noteOutputForm.get()));
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("notebooks", notebookService.getAllNotebooksByAccount(account));
        model.addAttribute("searchInputForm", new SearchInputForm());
        return "editNote";
    }

    @PutMapping(path = "/{id}/edit")
    @Transactional
    public String updateNote(Model model,
                             @ModelAttribute("currentNote") @Valid EditNoteInputForm editedNote,
                             BindingResult result,
                             Errors errors,
                             @PathVariable("notebookId") Integer notebookId,
                             @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        model.addAttribute("currentNote", noteService.updateNote(account, id, new Notebook(notebook), editedNote));
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("notebooks", notebookService.getAllNotebooksByAccount(account));
        model.addAttribute("searchInputForm", new SearchInputForm());
        return String.format("redirect:/notebook/%s/note/%s", notebookId, editedNote.getId());
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public String deleteNote(Model model,
                             @PathVariable("notebookId") Integer notebookId,
                             @PathVariable("id") Integer id) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        noteService.deleteNote(account, new Notebook(notebook), id);
        model.addAttribute("notes", notebook.getNotesAsForms());
        model.addAttribute("searchInputForm", new SearchInputForm());
        return String.format("redirect:/notebook/%s", notebookId);
    }

    @GetMapping(value = "/find", params = "by")
    @Transactional
    public String findNotesBy(Model model,
                              @PathVariable("notebookId") Integer notebookId,
                              @RequestParam(name = "by", required = true, defaultValue = "name") String searchParameter,
                              @ModelAttribute("searchInputForm") @Valid SearchInputForm searchInputForm) {
        if (searchInputForm.getSearchInput().isEmpty()) {
            return String.format("redirect:/notebook/%s", notebookId);
        }
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        model.addAttribute("account", account);
        model.addAttribute("notebook", notebook);
        Set<NoteOutputForm> result = new HashSet<>();
        switch (searchParameter) {
            case "name": {
                result = noteService.getAllNotesFromNotebookByName(account, new Notebook(notebook), searchInputForm.getSearchInput());
                break;
            }
            case "tags": {
                result = noteService.getAllNotesFromNotebookByTags(account, new Notebook(notebook),
                        TagFormUtils.getTagNamesAsSet(searchInputForm.getSearchInput()));
                break;
            }
            case "text": {
                result = noteService.getAllNotesFromNotebookByText(account, new Notebook(notebook), searchInputForm.getSearchInput());
                break;
            }
        }
        model.addAttribute("notes", result);
        model.addAttribute("searchInputForm", searchInputForm);
        return "notes";
    }

    @PostMapping(path = "/{id}/move", params = "to")
    @Transactional
    public String moveNoteToNotebook(Model model,
                                     @PathVariable("notebookId") Integer notebookId,
                                     @PathVariable("id") Integer id,
                                     @RequestParam(name = "to", required = true) Integer toNotebookId) {
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(notebookId);
        NotebookOutputForm toNotebook = notebookService.getById(toNotebookId);
        if (!account.equals(notebook.getAccount())) {
            throw new IllegalArgumentException("You can't get this notebook");
        }
        try {
            noteService.moveNoteToNotebook(account, id, new Notebook(notebook), new Notebook(toNotebook));
        } catch (IllegalArgumentException e) {
            return String.format("redirect:/notebook/%s/note/%s", notebookId, id);
        }
        return String.format("redirect:/notebook/%s/note/%s", toNotebookId, id);
    }
}

