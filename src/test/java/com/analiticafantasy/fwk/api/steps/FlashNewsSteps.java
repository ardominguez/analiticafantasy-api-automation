package com.analiticafantasy.fwk.api.steps;

import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.hook.TestContext;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlashNewsSteps extends BaseStepDefinition {

    private final TestContext<ApiResponse<?>> testContext;

    public FlashNewsSteps(TestContext<ApiResponse<?>> testContext) {
        this.testContext = testContext;
    }

    @Then("I send a GET request to get the news from feed")
    public void validateSquadRemoval() {
        var response = analiticaFantasyApiClient.getFlashNews(testContext.getAccessToken());
        assertEquals(200, response.getStatusCode(), "Flash news were not retrieved");

    }

}
