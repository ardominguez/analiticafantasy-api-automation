package com.analiticafantasy.fwk.api.steps;

import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileResponseDTO;
import com.analiticafantasy.fwk.api.hook.TestContext;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProfileSteps extends BaseStepDefinition {

    private final TestContext<ApiResponse<UserProfileResponseDTO>> testContext;

    public UserProfileSteps(TestContext<ApiResponse<UserProfileResponseDTO>> testContext) {
        this.testContext = testContext;
    }

    @When("User is data information is updated")
    public void User_is_data_information_is_updated() {
        var userProfileResponse = this.analiticaFantasyApiClient.getUserData(this.testContext.getAccessToken());
        assertEquals("asd", userProfileResponse.getBodyResponse().getNickname());
    }




}
