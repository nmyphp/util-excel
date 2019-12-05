package com.free.util.excel.apply;

import com.free.util.excel.comm.ExcelColumn;
import com.free.util.excel.comm.ExcelHead;
import com.free.util.excel.comm.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Excel 工具类
 */
public class ExcelUtil {

    private enum CellType {
        title, normal, error
    }

    private static final String MSG_INVALID_CONVERT = "数据类型转换失败";
    private static final String MSG_INVALID_ARGUMENT = "输入参数名不正确";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将Java实体集合转成Excel文档
     *
     * @param header     字段集合
     * @param desFile    目标文件的路径
     * @param oriObjects 被导出的实体集合
     */
    public static <T> void object2Excel(List<ExcelColumn> header, List<T> oriObjects, String desFile)
        throws Exception {
        File file = new File(desFile);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);
        try {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            DataFormat dataformat = wb.createDataFormat();
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 设置表头的样式 创建表头
            CellStyle titleStyle = createCellStyle(wb, dataformat, CellType.title, false);
            Row titleRow = sheet.createRow(0);
            titleRow.setHeight((short) 450);
            Cell cell = null;
            for (int i = 0, length = header.size(); i < length; i++) {
                cell = titleRow.createCell(i);
                cell.setCellStyle(titleStyle);
                RichTextString text = new XSSFRichTextString(header.get(i).getTitleName());
                cell.setCellValue(text);
            }
            // 设置单元格样式, 生成单元格
            int rowIndex = 1;
            Row cellrow = null;
            CellStyle cellStyle = createCellStyle(wb, dataformat, CellType.normal, false);
            for (int i = 0, length = oriObjects.size(); i < length; i++, rowIndex++) {
                cellrow = sheet.createRow(rowIndex);
                int cellIndex = 0;
                for (int j = 0, length2 = header.size(); j < length2; j++, cellIndex++) {
                    cell = cellrow.createCell(cellIndex);
                    cellStyle.setWrapText(header.get(j).isWrapTextType());
                    String fieldName = header.get(j).getFieldName();
                    cell.setCellType(header.get(j).getType());
                    setCell(cell, cellStyle, oriObjects.get(i), fieldName, dataformat);
                }
            }

            wb.write(out);
        } finally {
            out.close();
        }
    }

    /**
     * Excel文件转成Java对象集合
     *
     * @param excelHead Excel表头
     * @param oriFile   源文件
     * @param clazz     对象class
     */
    public static <T> List<T> excel2Object(String oriFile, ExcelHead excelHead, Class<T> clazz)
        throws Exception {

        File file = new File(oriFile);
        if (!file.exists()) {
            throw new FileNotFoundException("找不到文件" + oriFile);
        }
        Workbook wb = WorkbookFactory.create(copy(new FileInputStream(file)));
        Sheet sheet = wb.getSheetAt(0);
        // 校验Excel文件的列是否合法
        StringBuilder errorInfo = new StringBuilder();
        Row columnRow = sheet.getRow(excelHead.getStartTitleRow());
        List<ExcelColumn> colunms = excelHead.getColumns();
        for (int j = excelHead.getStartColumn(), length = excelHead.getColumns().size(); j < length; j++) {
            String cellValue = columnRow.getCell(j).getStringCellValue();
            String titleName = colunms.get(j).getTitleName();
            if (!cellValue.equals(titleName)) {
                String error = String.format("Excel列名和指定的列明无法对应[excelCellName:%s,ColumnName:%s]", cellValue, titleName);
                errorInfo.append(error).append("\n");
            }
        }
        if (errorInfo.length() > 0) {
            throw new RuntimeException(errorInfo.toString());
        }
        // 数据读取并转换
        List<T> result = new ArrayList<>(200);
        for (int i = excelHead.getStartDataRow(), length = sheet.getPhysicalNumberOfRows(); i < length; i++) {
            T entity = clazz.newInstance();
            Row row = sheet.getRow(i);
            // 跳过空行
            if (!emptyRow(row, excelHead.getStartColumn())) {
                parseExcelRow(row, excelHead, entity, DATE_PATTERN, errorInfo);
                result.add(entity);
            }
        }
        if (errorInfo.length() > 0) {
            throw new RuntimeException(errorInfo.toString());
        }
        return result;
    }

    /**
     * 更据传入列信息解析Excel的行到实体类中
     *
     * @param row        Excel的行
     * @param excelHead  上传文件的Excel列结构
     * @param entity     关联实体类class
     * @param dateFormat 日期格式如：yyyy-MM-dd HH:mm:ss
     * @param errorInfo  转换的错误信息
     */
    private static <T> void parseExcelRow(Row row, ExcelHead excelHead, T entity, String dateFormat,
                                          StringBuilder errorInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int startColumn = excelHead.getStartColumn();
        List<ExcelColumn> excelColunms = excelHead.getColumns();
        for (int j = startColumn, cellNum = row.getLastCellNum(); j < cellNum; j++) {
            String fieldName = excelColunms.get(j - startColumn).getFieldName();
            String fieldDispName = excelColunms.get(j - startColumn).getTitleName();
            String cellValue = getCellValue(row.getCell(j));
            if (StringUtil.isEmpty(cellValue)) {
                continue;
            }
            try {
                Field field = entity.getClass().getDeclaredField(fieldName);
                Class clsType = field.getType();
                field.setAccessible(true);
                if (String.class == clsType) {
                    field.set(entity, cellValue);
                } else if (Integer.class == clsType || "int".equals(clsType.getName())) {
                    field.set(entity, Integer.parseInt(cellValue));
                } else if (Long.class == clsType || "long".equals(clsType.getName())) {
                    field.set(entity, Long.parseLong(cellValue));
                } else if (Boolean.class == clsType || "boolean".equals(clsType.getName())) {
                    field.set(entity, Boolean.parseBoolean(cellValue));
                } else if (Float.class == clsType || "float".equals(clsType.getName())) {
                    field.set(entity, Float.parseFloat(cellValue));
                } else if (Double.class == clsType || "double".equals(clsType.getName())) {
                    field.set(entity, Double.parseDouble(cellValue));
                } else if (BigDecimal.class == clsType) {
                    field.set(entity, new BigDecimal(cellValue));
                } else if (Date.class == clsType) {
                    field.set(entity, sdf.parse(cellValue));
                } else if ("char".equals(clsType.getName())) {
                    field.set(entity, cellValue.charAt(0));
                } else {
                    field.set(entity, cellValue);
                }
            } catch (NoSuchFieldException ex) {
                errorInfo.append(MSG_INVALID_ARGUMENT)
                    .append("[")
                    .append(fieldDispName).append(":").append(cellValue)
                    .append("]")
                    .append(ex.getMessage())
                    .append("\n");
            } catch (Exception ex) {
                errorInfo.append(MSG_INVALID_CONVERT)
                    .append("[")
                    .append(fieldDispName).append(":").append(cellValue)
                    .append("]")
                    .append(ex.getMessage())
                    .append("\n");
            }
        }
    }

    /**
     * 更据单元格的类型获取对应的String值
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
            } else {
                cellValue = new DecimalFormat("#.##").format(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            cellValue = cell.getRichStringCellValue().getString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            cellValue = cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            cellValue = "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            cellValue = "";
        }
        return cellValue.trim();
    }

    /**
     * 更据类型创建单元格
     */
    private static CellStyle createCellStyle(Workbook wb, DataFormat dataformat, CellType type, boolean WrapTextType) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(dataformat.getFormat("@"));
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(WrapTextType);
        switch (type) {
            case title: {
                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                Font titleFont = wb.createFont();
                titleFont.setFontHeightInPoints((short) 11);
                titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(titleFont);
                break;
            }
            case normal: {
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                font.setFontHeightInPoints((short) 11);
                cellStyle.setFont(font);
                break;
            }
            case error: {
                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                font.setFontHeightInPoints((short) 11);
                cellStyle.setFont(font);
                break;
            }
            default:
        }
        return cellStyle;
    }

    /**
     * 填充单元格
     *
     * @param cell       单元格
     * @param cellStyle  单元格样式
     * @param entity     被填充的实体
     * @param fieldName  被填充的属性名
     * @param dataformat 数据格式
     */
    private static <T> void setCell(Cell cell, CellStyle cellStyle, T entity, String fieldName, DataFormat dataformat)
        throws Exception {
        Field field = null;
        Class<? extends Object> clz = entity.getClass();
        try {
            field = clz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = clz.getSuperclass().getDeclaredField(fieldName);
        }
        field.setAccessible(true);

        Object fieldValue = field.get(entity);
        if (fieldValue instanceof String) {
            cellStyle.setDataFormat(dataformat.getFormat("@"));
            cell.setCellValue(fieldValue.toString());
        } else if (fieldValue instanceof Integer || fieldValue instanceof Long) {
            cellStyle.setDataFormat(dataformat.getFormat("0"));
            cell.setCellValue(Double.parseDouble(fieldValue.toString()));
        } else if (fieldValue instanceof Double || fieldValue instanceof Float || fieldValue instanceof BigDecimal) {
            cellStyle.setDataFormat(dataformat.getFormat("0.00"));
            cell.setCellValue(Double.parseDouble(fieldValue.toString()));
        } else if (fieldValue instanceof Date) {
            String value = new SimpleDateFormat(DATE_PATTERN).format((Date) fieldValue);
            cell.setCellValue(value);
        } else {
            cellStyle.setDataFormat(dataformat.getFormat("@"));
            cell.setCellValue(fieldValue == null ? "" : fieldValue.toString());
        }

        cell.setCellStyle(cellStyle);
    }

    /**
     * 复制输入流
     *
     * @param in 输入流
     */
    private static InputStream copy(InputStream in) throws IOException {
        byte[] data = new byte[256];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = -1;
        try {
            while (-1 != (length = in.read(data))) {
                out.write(data, 0, length);
            }
            out.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } finally {
            out.close();
        }
    }

    /**
     * 检验excel是否空行
     */
    private static boolean emptyRow(Row row, int startColumn) {
        for (int j = startColumn; j < row.getLastCellNum(); j++) {
            String cellValue = getCellValue(row.getCell(j));
            if (!StringUtil.isEmpty(cellValue)) {
                return false;
            }
        }
        return true;
    }
}
