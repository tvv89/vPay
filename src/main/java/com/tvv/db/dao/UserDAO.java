package com.tvv.db.dao;

import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    User insertUser(User user) throws AppException;

    User findUserById(Long id) throws AppException;

    Map<String,String> findUserByAccountUID(String accountUID) throws AppException;

    List<User> findAllUsers() throws AppException;

    User findUserByLogin(String login) throws AppException;

    boolean checkUniqueLogin(String login) throws AppException;

    boolean updateStatusUserById(Long id, int status) throws AppException;

    boolean updateRoleUserById(Long id, int role) throws AppException;

    boolean updateLocalUserById(Long id, String local) throws AppException;
}
