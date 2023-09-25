package com.udacity.jwdnd.course1.cloudstorage.controller.impl;

import java.io.IOException;

import com.udacity.jwdnd.course1.cloudstorage.controller.IFileController;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.constant.Constants;
import com.udacity.jwdnd.course1.cloudstorage.service.IFileService;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import com.udacity.jwdnd.course1.cloudstorage.ultis.notificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class FileController implements IFileController {
    private final IFileService fileService;
    private final IUserService userService;
    private static final long maxFileSize = 5 * 1024 * 1024; // 5MB
    @PostMapping("/add-file")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws IOException {
        User user = getUserFromAuthentication(authentication);

        String uploadError = validateFileUpload(fileUpload, user.getUserId());
        if (uploadError != null) {
            int resultHandleService = 0;
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, "", uploadError);
        } else {
            int resultHandleService = uploadFileAndHandleExceptions(fileUpload, user.getUserId());
            notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.FILE_UPLOADED_SUCCESSFULLY, Constants.FILE_UPLOAD_FAILURE);
        }

        return "redirect:/home";
    }

    @GetMapping("delete-file/{id}")
    public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        int resultHandleService = fileService.deleteFile(id);
        notificationHandler.addFlashAttributeMessage(redirectAttributes, resultHandleService, Constants.FILE_DELETED_SUCCESSFULLY, Constants.FILE_DELETED_FAILURE);
        return "redirect:/home";
    }

    @GetMapping("download-file/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id) {
        File file = fileService.getFileById(id);
        HttpHeaders headers = createFileDownloadHeaders(file);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(file.getFileData()));
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName());
    }

    private String validateFileUpload(MultipartFile fileUpload, int userId) {
        if (fileUpload.isEmpty()) {
            return Constants.UPLOAD_ERROR;
        } else if (fileService.doesFileExist(fileUpload.getOriginalFilename(), userId)) {
            return Constants.FILE_ALREADY_EXISTS;
        } else {
            return null;

        }
    }

    private int uploadFileAndHandleExceptions(MultipartFile fileUpload, int userId) throws IOException, MaxUploadSizeExceededException {
        try {
            return fileService.uploadFile(fileUpload, userId);
        } catch (IOException | MaxUploadSizeExceededException e) {
            throw e;
        }
    }


    private HttpHeaders createFileDownloadHeaders(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.setContentDispositionFormData("attachment", file.getFileName());
        return headers;
    }

}
