package net.thumbtack.thumbnote.thumbnoteroot.spring.controller;

import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.input.NoteInputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NoteOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.output.NotebookOutputForm;
import net.thumbtack.thumbnote.thumbnoteroot.spring.request.CreateNoteRequest;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.AccountService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NoteService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.NotebookService;
import net.thumbtack.thumbnote.thumbnoteroot.spring.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NotebookService notebookService;

    @PostMapping(value = "/note/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> logInAndAddNote(@Valid @RequestBody CreateNoteRequest request,
                                   HttpServletResponse response) {
        securityService.autoLogin(request.getLogin(), request.getPassword());
        Account account = accountService.getCurrentAccount();
        NotebookOutputForm notebook = notebookService.getById(request.getNotebookId());
        NoteOutputForm currentNote = noteService.addNote(account, new Notebook(notebook), new NoteInputForm(request.getName(),
                                                                                                            request.getText(),
                                                                                                            request.getTags()));

        return ResponseEntity.ok().body(currentNote);
    }
}
