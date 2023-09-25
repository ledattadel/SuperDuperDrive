package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import com.udacity.jwdnd.course1.cloudstorage.constant.Constants;
import com.udacity.jwdnd.course1.cloudstorage.controller.ICredentialsController;
import com.udacity.jwdnd.course1.cloudstorage.controller.IHomeController;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.ICredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import com.udacity.jwdnd.course1.cloudstorage.ultis.notificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class CredentialsController implements ICredentialsController {

    private final ICredentialsService credentialsService;
    private final IUserService userService;
    private final IHomeController homeController;

    @GetMapping("/delete-credentials/{id}")
    public String deleteCredentials(@PathVariable int id, RedirectAttributes redirectAttributes) {
        int resultHandleService = credentialsService.deleteCredentialsById(id);
        notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.CREDENTIAL_DELETED_SUCCESSFULLY, Constants.CREDENTIAL_DELETED_FAILURE);
        return "redirect:/home";
    }

    @PostMapping("/save-credentials")
    public String saveCredentials(
            @ModelAttribute("credentialsFrom") Credentials credentials, Model model,
            Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        User user = userService.getUserByUsername(authentication.getName());

        int resultHandleService = 0;
        if (credentials.getCredentialId() != null) {
            resultHandleService = credentialsService.updateCredentials(credentials);
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.CREDENTIAL_UPDATED_SUCCESSFULLY, Constants.CREDENTIAL_UPDATED_FAILURE);
        } else {
            credentials.setUserId(user.getUserId());
            resultHandleService = credentialsService.addCredentials(credentials);
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.CREDENTIAL_CREATED_SUCCESSFULLY, Constants.CREDENTIAL_CREATED_FAILURE);
        }

        model.addAttribute("credentialsList", credentialsService.getCredentialsByUserId(user.getUserId()));
        return "redirect:/home";
    }

}
