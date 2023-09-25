package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import com.udacity.jwdnd.course1.cloudstorage.controller.IHomeController;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.ICredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.IFileService;
import com.udacity.jwdnd.course1.cloudstorage.service.INoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class HomeController implements IHomeController {

    private final IUserService userService;
    private final INoteService noteService;
    private final IFileService fileService;
    private final ICredentialsService credentialsService;

    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication) throws IOException {
        User user = getUserFromAuthentication(authentication);
        if (user == null) {
            return redirectToLogin();
        }
        addAttributesToModel(model, user);

        return "home";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName());
    }

    private String redirectToLogin() {
        return "redirect:/login";
    }

    private void addAttributesToModel(Model model, User user) throws IOException {
        int userId = user.getUserId();

        model.addAttribute("fileList", fileService.getFilesByUserId(userId));
        model.addAttribute("credentialsList", credentialsService.getCredentialsByUserId(userId));
        model.addAttribute("user", userService.getUserByUsername(user.getUsername()));
        model.addAttribute("noteList", noteService.getNotesByUserId(userId));
    }

}
