package com.free.util.excel.demo.controller.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result(T data) {
        this.code = 0;
        this.data = data;
    }
}
