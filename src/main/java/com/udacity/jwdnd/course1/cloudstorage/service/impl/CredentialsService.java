package com.udacity.jwdnd.course1.cloudstorage.service.impl;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.ICredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.processor.StandardUnlessTagProcessor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CredentialsService implements ICredentialsService {
    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;
    private final IUserService userService;

    @Override
    public List<Credentials> getAllCredentials() {
        List<Credentials> credentialsList = credentialsMapper.getAllCredentials();

        return credentialsList.stream()
                .map(credentials -> {

                    credentials.setDecryptValue(decryptPassword(credentials.getPassword(), credentials.getKey()));
                    return credentials;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int addCredentials(Credentials credentials) {
        byte[] salt = generateSalt();
        String encodedSalt = encodeSalt(salt);
        String encryptedPassword = encryptPassword(credentials.getPassword(), salt);
        credentials.setKey(encodedSalt);
        credentials.setPassword(encryptedPassword);

        return credentialsMapper.insertCredentials(credentials);
    }


    @Override
    public int updateCredentials(Credentials credentials) {
        Credentials existingCredentials = credentialsMapper.getCredentialById(credentials.getCredentialId());
        credentials.setCredentialId(existingCredentials.getCredentialId());

        byte[] salt = generateSalt();
        String encodedSalt = encodeSalt(salt);
        String encryptedPassword = encryptPassword(credentials.getPassword(), salt);
        credentials.setKey(encodedSalt);
        credentials.setPassword(encryptedPassword);
        credentials.setUserId(existingCredentials.getUserId());

        return credentialsMapper.updateCredentials(credentials);
    }

    @Override
    public int deleteCredentialsById(int credentialsId) {
        return credentialsMapper.deleteCredentials(credentialsId);
    }


    @Override
    public List<Credentials> getCredentialsByUserId(int userId) {
        List<Credentials> credentialsList = credentialsMapper.getCredentialsByUserId(userId);

        return credentialsList.stream()
                .map(credentials -> {
                    credentials.setDecryptValue(decryptPassword(credentials.getPassword(), credentials.getKey()));
                    return credentials;
                })
                .collect(Collectors.toList());
    }


    @Override
    public boolean doesUserHaveCredentials(String userName) {
        User user = userService.getUserByUsername(userName);
        List<Credentials> credentialsList = credentialsMapper.getCredentialsByUserId(user.getUserId());
        return !credentialsList.isEmpty();
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String encodeSalt(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }

    private String encryptPassword(String password, byte[] salt) {
        return encryptionService.encryptValue(password, encodeSalt(salt));
    }

    private String decryptPassword(String encryptedPassword, String salt) {
        return encryptionService.decryptValue(encryptedPassword, salt);
    }
}