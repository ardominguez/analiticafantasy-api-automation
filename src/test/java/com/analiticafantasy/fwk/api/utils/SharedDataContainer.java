package com.analiticafantasy.fwk.api.utils;

import lombok.Data;
import org.picocontainer.Disposable;

@Data
public class SharedDataContainer implements Disposable {

    private String accessToken;

    @Override
    public void dispose() {
    }
}
