package com.dang.web_test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dang.web_test.dao.UserDao;
import com.dang.web_test.dto.User;
import com.dang.web_test.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public List<User> getAllUser() {
		return userDao.findAll();
	}

}
