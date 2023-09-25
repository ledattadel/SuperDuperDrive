package com.udacity.jwdnd.course1.cloudstorage.service;

import java.util.List;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;

public interface ICredentialsService {
    List<Credentials> getAllCredentials();

    int addCredentials(Credentials credentials);

    int updateCredentials(Credentials credentials);

    int deleteCredentialsById(int credentialsId);

    List<Credentials> getCredentialsByUserId(int userId);

    boolean doesUserHaveCredentials(String userName);
}
