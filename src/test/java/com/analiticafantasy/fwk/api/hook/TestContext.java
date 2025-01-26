package com.analiticafantasy.fwk.api.hook;

import lombok.Data;

@Data
public class TestContext<T> {

    private String accessToken;
    private T data;
}
