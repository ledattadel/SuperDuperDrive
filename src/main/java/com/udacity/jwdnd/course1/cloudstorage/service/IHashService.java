package com.udacity.jwdnd.course1.cloudstorage.service;
public interface IHashService {
    String getHashedValue(String data, String salt);
}
