### 功能
包装Apache-pio API对外提供以下静态方法：

- 将Java实体集合转成Excel文档

```java
public static <T> void object2Excel(List<ExcelColumn> header, List<T> oriObjects, String desFile)
```

- Excel文件转成Java对象集合

```java
public static <T> TransResult<T> excel2Object(String oriFile, ExcelHead excelHead, Class<T> clazz)
```