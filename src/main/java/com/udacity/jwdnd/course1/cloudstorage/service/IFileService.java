package com.udacity.jwdnd.course1.cloudstorage.service;

import org.springframework.web.multipart.MultipartFile;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import java.io.IOException;
import java.util.List;

public interface IFileService {
    List<File> getFilesByUserId(Integer userId) throws IOException;

    int uploadFile(MultipartFile fileUpload, Integer userId) throws IOException;

    boolean doesFileExist(String fileName, Integer userId);

    int deleteFile(Integer fileId);

    File getFileById(Integer fileId);

    File createFileFromMultipartFile(MultipartFile fileUpload, Integer userId) throws IOException;
}
