package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface INoteController {
    String deleteNote(int id, RedirectAttributes redirectAttributes);
    String saveNote(
            Note note, Model model, Authentication authentication, RedirectAttributes redirectAttributes);
}
