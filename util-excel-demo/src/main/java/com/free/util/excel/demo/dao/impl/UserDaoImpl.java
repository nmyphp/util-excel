package com.free.util.excel.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.free.util.excel.demo.dao.UserDao;
import com.free.util.excel.demo.dao.base.BaseDao;
import com.free.util.excel.demo.dao.po.User;

@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao{

	@Override
	public List<User> findAll() {
		return super.getSqlSessionTemplate().selectList(super.getStatementNamespace() + ".findAll");
	}
}
