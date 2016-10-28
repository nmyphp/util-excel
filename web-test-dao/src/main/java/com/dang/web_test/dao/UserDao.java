package com.dang.web_test.dao;

import java.util.List;
import com.dang.web_test.dto.User;


public interface UserDao{

    public List<User> findAll();

}
