<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>home page</title>
</head>
<body>
	<div>Welcome to test page! We will test the functions :<br>
		<ul>
			<li>1. Download the template <a href="downloadTemplate.do">下载模板</a></li>
			<li>2. Do import data from excel to object <a href="javascript:void(0)" onclick="upload();">上传</a></li>
			<li>3. Do export data from object to excel <a href="export.do">导出</a></li>
			<li>4. Download the error file <a href="downloadError.do">下载错误文件</a></li>
		</ul>
	</div>
	<div id="form_div" style="display:none">
		<form id="form1" method="post" enctype="multipart/form-data" action="import.do">  
          	请选择文件：<input type="file" name="uploadExcel">
			<input type="submit" value="提交">
        </form>
	</div>
	
	<script type="text/javascript" src="<s:url value="/js/jquery.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/js/test.js"/>"></script>
</body>
</html>