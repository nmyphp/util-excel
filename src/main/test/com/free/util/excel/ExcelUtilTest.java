package com.free.util.excel;

import com.free.util.excel.apply.ExcelUtil;
import com.free.util.excel.comm.ExcelColumn;
import com.free.util.excel.comm.ExcelHead;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelUtilTest {

    private UserService userService = new UserService();
    private static List<ExcelColumn> excelColumns = new ArrayList<>();

    static {
        excelColumns.add(new ExcelColumn("id", "编号"));
        excelColumns.add(new ExcelColumn("name", "姓名"));
        excelColumns.add(new ExcelColumn("sex", "性别"));
        excelColumns.add(new ExcelColumn("age", "年龄"));
        excelColumns.add(new ExcelColumn("score", "分数"));
        excelColumns.add(new ExcelColumn("birthday", "生日"));
        excelColumns.add(new ExcelColumn("tuition", "出场费"));
    }

    @Test
    public void object2Excel() throws Exception {
        List<User> users = userService.getAllUser();
        ExcelUtil.object2Excel(excelColumns, users, "/Users/chenlong/User.xlsx");
    }

    @Test
    public void excel2Object() throws Exception {
        ExcelHead excelHead = new ExcelHead();
        excelHead.setColumns(excelColumns);
        List<User> transResult = ExcelUtil.excel2Object("/Users/chenlong/User.xlsx", excelHead, User.class);
        System.out.println(new Gson().toJson(transResult));
    }
}