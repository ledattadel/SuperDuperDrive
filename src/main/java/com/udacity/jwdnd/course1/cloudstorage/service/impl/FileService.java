package com.udacity.jwdnd.course1.cloudstorage.service.impl;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final FileMapper fileMapper;

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public int uploadFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File file = createFileFromMultipartFile(fileUpload, userId);
        return fileMapper.addFile(file);
    }

    public boolean doesFileExist(String fileName, Integer userId) {
        File file = fileMapper.getFileByFileNameAndUserId(fileName, userId);
        if (file == null){
            return false;
        }else {
            return true;
        }
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFileById(fileId);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public File createFileFromMultipartFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File file = new File();
        file.setContentType(fileUpload.getContentType());
        file.setFileData(fileUpload.getBytes());
        file.setFileName(fileUpload.getOriginalFilename());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setUserId(userId);
        return file;
    }
}