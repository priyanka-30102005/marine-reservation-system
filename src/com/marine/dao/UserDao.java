package com.marine.dao;

import com.marine.bean.UserBean;

public interface UserDao {
    boolean registerUser(UserBean user);
    String validateUser(UserBean user);
    int getUserIdByUsername(String username);
}
