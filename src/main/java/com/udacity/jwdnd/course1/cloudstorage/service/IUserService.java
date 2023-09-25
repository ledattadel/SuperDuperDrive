package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.User;

public interface IUserService {
    boolean isUsernameAvailable(String username);
    int createUser(User user);
    User getUserByUsername(String username);
}
