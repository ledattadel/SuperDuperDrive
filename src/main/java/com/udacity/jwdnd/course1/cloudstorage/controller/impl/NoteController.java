package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import com.udacity.jwdnd.course1.cloudstorage.constant.Constants;
import com.udacity.jwdnd.course1.cloudstorage.controller.INoteController;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.INoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import com.udacity.jwdnd.course1.cloudstorage.ultis.notificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class NoteController implements INoteController {

    private final INoteService noteService;
    private final IUserService userService;


    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable int id, RedirectAttributes redirectAttributes) {
        int resultHandleService = noteService.deleteNoteById(id);
        notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.NOTE_DELETED_SUCCESSFULLY, Constants.NOTE_DELETED_FAILURE);

        return "redirect:/home";
    }


    @PostMapping("/save-note")
    public String saveNote(
            @ModelAttribute("noteForm") Note note, Model model,
            Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(authentication.getName());


        if (note.getNoteId() != null) {
            int resultHandleService = noteService.updateNote(note, note.getNoteId());
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.NOTE_UPDATED_SUCCESSFULLY, Constants.NOTE_UPDATED_FAILURE);

        } else if (note.getNoteId() == null) {
            note.setUserId(user.getUserId());
            int resultHandleService = noteService.createNote(note);
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.NOTE_CREATED_SUCCESSFULLY, Constants.NOTE_CREATED_FAILURE);

        }

        model.addAttribute("noteList", noteService.getNotesByUserId(user.getUserId()));
        return "redirect:/home";
    }
}
