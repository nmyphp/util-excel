package com.free.util.excel.demo.service.impl;

import com.free.util.excel.demo.dao.UserDao;
import com.free.util.excel.demo.dao.po.User;
import com.free.util.excel.demo.service.UserService;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public List<User> getAllUser() {
		return userDao.findAll();
	}

}
