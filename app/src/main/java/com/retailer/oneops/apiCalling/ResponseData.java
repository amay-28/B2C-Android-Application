package com.retailer.oneops.apiCalling;

import lombok.Data;

@Data
public class ResponseData<T> {
    private String name;
    private String type;
    private String message;
    private T data;
    private T image;
    private Boolean success;
}
