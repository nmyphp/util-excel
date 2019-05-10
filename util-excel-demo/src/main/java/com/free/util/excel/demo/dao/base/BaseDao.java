package com.free.util.excel.demo.dao.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;

public class BaseDao<T> {

	private String namespace;
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public String getStatementNamespace(){
		return this.namespace;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return this.sqlSessionTemplate;
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType pType = (ParameterizedType) type;
		Type[] params = pType.getActualTypeArguments();
		Class<T> cls = (Class<T>) params[0];
		namespace =  cls.getName();
	}
	
}
