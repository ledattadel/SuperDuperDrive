package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

public interface IFileController {
    String addFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws IOException, IOException;

    String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes);

    ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id);

}
