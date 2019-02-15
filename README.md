### Function
包装Apache-pio API对外提供以下静态方法：

1.将Java实体集合转成Excel文档

```java
void trans2Excel(List<ExcelColumn> excelColumns, HttpServletResponse response, List<T> entities)
```
2.生成上传Excel文件对应的错误文件，在原始上传文件中追加操作和错误日志，保存到服务端，返回文件名供下载

```java
String generateErrorExcel(CommonsMultipartFile multiFile, String exportPath, ExcelHead excelHead, 
List<ConvertResult<T>> entities)
```
3.CommonsMultipartFile转成Java对象集合

```java
TransResult<T> trans2Object(CommonsMultipartFile multiFile, ExcelHead excelHead, Class<T> clz)
```
4.下载Excel文件

```java
void downloadExcel(HttpServletResponse response, String filePath)
```
### Getting Start
1.模板下载

Controller

```java
@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
public void downloadTemplate(HttpServletResponse response){
  try{
  String filePath = ClassUtils.getDefaultClassLoader().getResource("/template").getPath() + File.separator + "template.xlsx";
  ExcelUtil.downloadExcel(response, filePath);
  }catch(Exception e){
    logger.error(e.getMessage(),e);
  }
}
```
模板excel存放到template目录即可
2.文件上传

test.jsp

```jsp
<div id="form_div" style="display:none">
  <form id="form1" method="post" enctype="multipart/form-data" action="import.do">  
          请选择文件：<input type="file" name="uploadExcel">
    <input type="submit" value="提交">
      </form>
</div>
```
将excel的行数据装成对象集合

Controller

```java
@RequestMapping(value = "/import", method = RequestMethod.POST)
public void import2(@RequestParam("uploadExcel") CommonsMultipartFile uploadExcel, HttpServletRequest request){
  try{
    List<ExcelColumn> excelColumns = new ArrayList<> ();
    excelColumns.add(new ExcelColumn("id","编号"));
    excelColumns.add(new ExcelColumn("name","姓名"));
    ExcelHead excelHead = new ExcelHead();
    excelHead.setColumns(excelColumns);
    TransResult<User> result = ExcelUtil.trans2Object(uploadExcel, excelHead, User.class);
    if(!result.getSuccess()){
      logger.error(new ObjectMapper().writeValueAsString(result));
    }else{
      logger.info(new ObjectMapper().writeValueAsString(result));
    }
  }catch(Exception e){
    logger.error(e.getMessage(),e);
  }
}
```
2.导出

将对象集合导出成excel行数据

Controller

```java
@RequestMapping(value = "/export", method = RequestMethod.GET)
public void export(HttpServletResponse response){
  try{
    List<ExcelColumn> excelColumns = new ArrayList<> ();
    excelColumns.add(new ExcelColumn("id","编号"));
    excelColumns.add(new ExcelColumn("name","姓名"));
    List<User> users = userService.getAllUser();
    ExcelUtil.trans2Excel(excelColumns, response, users);
  }catch(Exception e){
    logger.error(e.getMessage(),e);
  }
}
```

>更多详细内容请参考util-excel-demo，运行util-excel-demo，然后访问：http://localhost:8080/test

![运行示例](https://github.com/nmyphp/util-excel/blob/master/doc/demo.jpg)