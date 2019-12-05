package com.free.util.excel.comm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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

    public ExcelColumn(String fieldName, String titleName) {
        super();
        this.fieldName = fieldName;
        this.titleName = titleName;
    }
}
