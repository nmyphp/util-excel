package com.free.util.excel.comm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExcelColumn {
    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * excel显示名称
     */
    private String titleName;

    /**
     * 字段类型
     */
    private int type;
    /**
     * 是否换行类型
     */
    private boolean wrapTextType;

    public ExcelColumn() {

    }

    public ExcelColumn(String fieldName) {
        super();
        this.fieldName = fieldName;
    }

    public ExcelColumn(String fieldName, String titleName) {
        super();
        this.fieldName = fieldName;
        this.titleName = titleName;
    }

    public ExcelColumn(String fieldName, String titleName, int type) {
        super();
        this.fieldName = fieldName;
        this.titleName = titleName;
        this.type = type;
    }

    public ExcelColumn(String fieldName, String titleName, int type, boolean wrapTextType) {
        super();
        this.fieldName = fieldName;
        this.titleName = titleName;
        this.type = type;
        this.wrapTextType = wrapTextType;
    }

    public ExcelColumn(String fieldName, String titleName, boolean wrapTextType) {
        super();
        this.fieldName = fieldName;
        this.titleName = titleName;
        this.wrapTextType = wrapTextType;
    }
}
