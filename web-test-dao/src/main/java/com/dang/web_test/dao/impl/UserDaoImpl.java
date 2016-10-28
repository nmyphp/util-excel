package com.dang.web_test.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dang.web_test.dao.UserDao;
import com.dang.web_test.dao.base.BaseDao;
import com.dang.web_test.dto.User;

@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao{

	@Override
	public List<User> findAll() {
		return super.getSqlSessionTemplate().selectList(super.getStatementNamespace() + ".findAll");
	}
}
