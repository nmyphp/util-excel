package com.free.util.excel.comm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExcelHead {

    /**
     * 列信息
     */
    private List<ExcelColumn> columns;

    /**
     * 标题开始行
     */
    private int startTitleRow = 0;

    /**
     * 数据开始行
     */
    private int startDataRow = 1;

    /**
     * 标题和数据开始列
     */
    private int startColumn = 0;

    /**
     * tilte名
     */
    private String titleName;

    /**
     * sheet名
     */
    private String sheetName;
}