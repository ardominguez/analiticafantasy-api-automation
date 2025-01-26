package com.analiticafantasy.fwk.api.steps;
import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.auth.AuthRequestDTO;
import com.analiticafantasy.fwk.api.api.auth.AuthResponseDTO;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.hook.TestContext;
import com.analiticafantasy.fwk.api.utils.ConfigurationsLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class LoginSteps extends BaseStepDefinition {
    private final TestContext<ApiResponse<AuthResponseDTO>> testContext;

    private static final String LOGIN_EMAIL_KEY = "analiticafantasy.login.%s.email";
    private static final String LOGIN_PASSWORD_KEY = "analiticafantasy.login.%s.password";
    private static final String INVALID_MESSAGE_KEY = "analiticafantasy.login.invalid.message";
    private static final String USER_LOGGED = "analiticafantasy.login.valid.email";
    private static final int SUCCESS_STATUS = 200;
    private static final int UNAUTHORIZED_STATUS = 401;

    public LoginSteps(TestContext<ApiResponse<AuthResponseDTO>> testContext) {
        this.testContext = testContext;
    }

    @Given("I do login api authentication with credentials {string}")
    public void I_do_login(String credentials) {
        performLogin(credentials);
    }

    @Then("I get a login error message")
    public void verifyLoginErrorMessage() {
        String invalidMessage = ConfigurationsLoader.getConfigValue(INVALID_MESSAGE_KEY);

        ApiResponse<AuthResponseDTO> response = testContext.getData();
        assertEquals(invalidMessage, response.getTextResponse());
    }

    @Then("User is properly logged")
    public void verifySuccessfulLogin() {
        String username = ConfigurationsLoader.getConfigValue(USER_LOGGED);
        ApiResponse<AuthResponseDTO> response = testContext.getData();

        assertEquals("true", response.getBodyResponse().getLogged());
        assertEquals(username, response.getBodyResponse().getEmail());
    }

    private void performLogin(String credentialType) {

        String userEmail = String.format(LOGIN_EMAIL_KEY, credentialType);
        String userPassword = String.format(LOGIN_PASSWORD_KEY, credentialType);

        String username = ConfigurationsLoader.getConfigValue(userEmail);
        String password = ConfigurationsLoader.getConfigValue(userPassword);
        int responseStatusCode = credentialType.equals("valid") ? SUCCESS_STATUS : UNAUTHORIZED_STATUS;

        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail(username);
        authRequestDTO.setPassword(password);

        ApiResponse<AuthResponseDTO> apiResponse = this.analiticaFantasyApiClient.loginUser(authRequestDTO);
        assertEquals(responseStatusCode, apiResponse.getStatusCode());
        testContext.setData(apiResponse);

    }

}