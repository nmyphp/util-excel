package com.free.util.excel.comm;

/**
 * 操作类型枚举
 */
public enum OperationType {
    // 数据转换
    DATE_PARSE(0, "数据转换"),
    // 数据持久化
    DATA_PERSISTENCE(1, "数据持久化"),
    // 数据校验
    DATA_CHECK(2, "数据校验"),
    // 其它
    OTHER(3, "其它");

    private int code;
    private String desc;

    private OperationType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
