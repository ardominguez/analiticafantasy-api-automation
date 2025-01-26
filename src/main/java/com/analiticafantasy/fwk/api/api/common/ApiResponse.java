package com.analiticafantasy.fwk.api.api.common;

import lombok.Data;

import org.w3c.dom.Document;

@Data
public class ApiResponse<T> {

    private int statusCode;
    private T bodyResponse;
    private String textResponse;
    private Document xmlResponse;
}
