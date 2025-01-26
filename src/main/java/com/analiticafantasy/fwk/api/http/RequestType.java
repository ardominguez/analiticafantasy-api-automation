package com.analiticafantasy.fwk.api.http;

public enum RequestType {

    GET, POST, PUT, DELETE, PATCH;

    public boolean isGet () {
        return this == GET;
    }
}
