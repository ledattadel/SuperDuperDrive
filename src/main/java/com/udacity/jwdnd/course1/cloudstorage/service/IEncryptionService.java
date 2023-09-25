package com.udacity.jwdnd.course1.cloudstorage.service;

public interface IEncryptionService {
    String encryptValue(String data, String key);

    String decryptValue(String data, String key);
}
