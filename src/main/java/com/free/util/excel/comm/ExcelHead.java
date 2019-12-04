package com.free.util.excel.comm;

import java.util.List;

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

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public List<ExcelColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ExcelColumn> columns) {
        this.columns = columns;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getStartTitleRow() {
        return startTitleRow;
    }

    public void setStartTitleRow(int startTitleRow) {
        this.startTitleRow = startTitleRow;
    }

    public int getStartDataRow() {
        return startDataRow;
    }

    public void setStartDataRow(int startDataRow) {
        this.startDataRow = startDataRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    @Override
    public String toString() {
        return "ExcelHead [startColumn=" + startColumn + ", startTitleRow=" + startTitleRow + ", startDataRow="
            + startDataRow + ", titleName=" + titleName + "]" + ", columns="
            + columns + "]";
    }

}