### 包装Apache-pio API对外提供以下静态方法：

#### 1. 将Java实体集合转成Excel文档
```java
void trans2Excel(List<ExcelColumn> excelColumns, HttpServletResponse response, List<T> entities)
```
#### 2. 生成上传Excel文件对应的错误文件，在原始上传文件中追加操作和错误日志，保存到服务端，返回文件名供下载
```java
String generateErrorExcel(CommonsMultipartFile multiFile, String exportPath, ExcelHead excelHead, 
List<ConvertResult<T>> entities)
```
#### 3. CommonsMultipartFile转成Java对象集合
```java
TransResult<T> trans2Object(CommonsMultipartFile multiFile, ExcelHead excelHead, Class<T> clz)
```
#### 4.下载Excel文件
```java
void downloadExcel(HttpServletResponse response, String filePath)
```
