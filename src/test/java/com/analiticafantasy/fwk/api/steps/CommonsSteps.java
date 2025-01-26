package com.analiticafantasy.fwk.api.steps;
import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.auth.AuthRequestDTO;
import com.analiticafantasy.fwk.api.api.auth.AuthResponseDTO;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.hook.TestContext;
import com.analiticafantasy.fwk.api.utils.ConfigurationsLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.cucumber.java.en.Given;
public class CommonsSteps extends BaseStepDefinition {

    private static final String VALID_USERNAME_KEY = "analiticafantasy.login.valid.email";
    private static final String VALID_PASSWORD_KEY = "analiticafantasy.login.valid.password";

    public CommonsSteps(TestContext<?> testContext) {
        this.testContext = testContext;
    }

    @Given("I get the API access token through the login process")
    public void obtainApiAccessToken() {
        ApiResponse<AuthResponseDTO> loginResponse = loginWithValidCredentials();

        assertEquals(200, loginResponse.getStatusCode(), "Login request failed with unexpected status code.");

        String token = loginResponse.getBodyResponse().getToken();
        testContext.setAccessToken(token);
    }

    /**
     * Helper method to perform a login with valid credentials.
     *
     * @return ApiResponse containing the authentication response
     */
    private ApiResponse<AuthResponseDTO> loginWithValidCredentials() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail(ConfigurationsLoader.getConfigValue(VALID_USERNAME_KEY));
        authRequestDTO.setPassword(ConfigurationsLoader.getConfigValue(VALID_PASSWORD_KEY));
        return analiticaFantasyApiClient.loginUser(authRequestDTO);
    }


}
