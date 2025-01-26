package com.analiticafantasy.fwk.api.steps;

import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileUpdateRequestDTO;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileUpdateResponseDTO;
import com.analiticafantasy.fwk.api.hook.TestContext;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateUserProfileSteps extends BaseStepDefinition {

    private final TestContext<ApiResponse<UserProfileUpdateResponseDTO>> testContext;

    public UpdateUserProfileSteps(TestContext<ApiResponse<UserProfileUpdateResponseDTO>> testContext) {
        this.testContext = testContext;
    }

    @When("User update the data information")
    public void user_update_the_data_information() {
        UserProfileUpdateRequestDTO profileRequestDTO = new UserProfileUpdateRequestDTO();
        profileRequestDTO.setUsername("ailen.ramayo.dominguez@gmail.com");
        profileRequestDTO.setNickname("asd");
        profileRequestDTO.setUserSocial(null);

        ApiResponse<UserProfileUpdateResponseDTO> updateUserProfileResponse = this.analiticaFantasyApiClient.updateUserData(this.testContext.getAccessToken(), profileRequestDTO);
        assertEquals(200, updateUserProfileResponse.getStatusCode());
        assertEquals("User updated successfully", updateUserProfileResponse.getTextResponse());
        testContext.setData(updateUserProfileResponse);
    }

}
