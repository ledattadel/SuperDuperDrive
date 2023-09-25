package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

public interface ICredentialsController {

    String deleteCredentials(int id, RedirectAttributes redirectAttributes);

    String saveCredentials(
            Credentials credentials, Model model,
            Authentication authentication, RedirectAttributes redirectAttributes) throws IOException;

}
