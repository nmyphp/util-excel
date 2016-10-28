package com.dang.web_test.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import util.excel.apply.ExcelUtil;
import util.excel.comm.ExcelColumn;
import util.excel.comm.ExcelHead;
import util.excel.comm.ImportResult;

import com.dang.web_test.dto.User;
import com.dang.web_test.service.UserService;


@Controller
public class UserController {
    // 日志
    private static final Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/showuser", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView listUser(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView view = new ModelAndView("/index");
    	try{
	    	List<User> users = userService.getAllUser();
	    	view.addObject("users", users);
	    	return view;
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		return view;
    	}
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(){
    	ModelAndView view = new ModelAndView("/test");
    	return view;
    }
    
    @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response){
    	try{
    	    ExcelUtil.getTemplate(response);
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
    
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void import2(@RequestParam("uploadExcel") CommonsMultipartFile uploadExcel, HttpServletRequest request){
    	try{
    		String errorFilePath = request.getSession().getServletContext().getRealPath("/") + "errorExcel";
    		List<ExcelColumn> excelColumns = new ArrayList<ExcelColumn> ();
    		excelColumns.add(new ExcelColumn("id","编号"));
    		excelColumns.add(new ExcelColumn("name","姓名"));
    		ExcelHead excelHead = new ExcelHead();
    		excelHead.setColumns(excelColumns);
    		ImportResult<User> result = ExcelUtil.importExcel(uploadExcel, excelHead, request, User.class, errorFilePath);
    		if(!result.getSuccess()){
    			logger.error(new ObjectMapper().writeValueAsString(result));
    		}else{
    			logger.info(new ObjectMapper().writeValueAsString(result));
    		}
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
    
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response){
    	try{
    		List<ExcelColumn> excelColumns = new ArrayList<ExcelColumn> ();
    		excelColumns.add(new ExcelColumn("id","编号"));
    		excelColumns.add(new ExcelColumn("name","姓名"));
    		List<User> users = userService.getAllUser();
    		ExcelUtil.export2Excel(excelColumns, response, users);
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
    
    @RequestMapping(value = "/downloadError", method = RequestMethod.GET)
    public void downloadError(String errorFileName, HttpServletRequest request, HttpServletResponse response){
    	try{
    		String errorPath = request.getSession().getServletContext().getRealPath("/") + "errorExcel";
    		String errorFile = errorPath + File.separator + errorFileName;
    		ExcelUtil.getErrorExcel(response, errorFile);
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
}
