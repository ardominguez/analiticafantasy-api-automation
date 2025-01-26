package com.analiticafantasy.fwk.api;

import com.analiticafantasy.fwk.api.hook.TestContext;
import com.analiticafantasy.fwk.api.http.AnaliticaFantasyApiClient;


public class BaseStepDefinition {

    protected AnaliticaFantasyApiClient analiticaFantasyApiClient = new AnaliticaFantasyApiClient();
    protected TestContext<?> testContext;
}
