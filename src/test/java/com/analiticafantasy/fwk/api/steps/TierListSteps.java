package com.analiticafantasy.fwk.api.steps;

import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.api.tierlist.*;
import com.analiticafantasy.fwk.api.hook.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TierListSteps extends BaseStepDefinition {

    private final TestContext<ApiResponse<?>> testContext;

    public TierListSteps(TestContext<ApiResponse<?>> testContext) {
        this.testContext = testContext;
    }

    @When("I send a POST request to create a TierList")
    public void createTierList() {
        TierListCreationRequestDTO tierListRequest = buildTierListRequest(null, "TierList RealMadrid", "TierList para pruebas", List.of(), defaultTierList(), false, false);
        ApiResponse<TierListCreationResponseDTO> response = this.analiticaFantasyApiClient.upsertTierList(testContext.getAccessToken(), tierListRequest);

        assertEquals(200, response.getStatusCode(), "TierList creation failed.");
        testContext.setData(response);
    }

    @Then("TierList is created properly")
    public void validateTierListIsCreated() {
        validateTextResponse("Tierlist creada con éxito");
    }

    @And("I send a GET request to get the tierList info")
    public void validateGetTierListInfo() {
        ApiResponse<TierListResponseDTO> response = this.analiticaFantasyApiClient.getTierList(testContext.getAccessToken());

        validateStatusCodeResponse(response, 200, "TierList were not returned.");

        var tierListOpt = response.getBodyResponse().getTierlists().stream().filter(t->t.getTitle().equals("TierList RealMadrid")).findFirst();
        assertTrue(tierListOpt.isPresent(), "Squad not found");

        var tierList = tierListOpt.get();

        ApiResponse<TierListInfoDTO>  tierListContext = new ApiResponse<>();
        tierListContext.setBodyResponse(tierList);

        testContext.setData(tierListContext);
    }

    @Then("I send a GET request to visualize the tierList")
    public void validateGetTierListVisualize() {

        TierListInfoDTO TierListInfo = getResponseData(TierListInfoDTO.class);

        ApiResponse<ViewTierListResponseDTO> response = this.analiticaFantasyApiClient.getTierListInfo(testContext.getAccessToken(), TierListInfo.getSlug());

        validateNotNullResponse(response);

        validateStatusCodeResponse(response, 200, "TierList were not returned.");

        setTestContextData(response);
    }


    @And("I send a REMOVE request to eliminate the tierList")
    public void validateRemoveTierList() {
        TierListInfoDTO tierListInfo = getResponseData(TierListInfoDTO.class);
        ApiResponse<Void> response = this.analiticaFantasyApiClient.removeTierList(testContext.getAccessToken(), tierListInfo.getId());

        validateStatusCodeResponse(response, 200, "TierList was not removed.");

        validateTextResponse("Tierlist eliminada con éxito", response, "TierList removed return a wrong message");
    }

    @And("I delete the tierList")
    public void validateDeleteTierList() {
        ViewTierListResponseDTO tierList = getResponseData(ViewTierListResponseDTO.class);
        ApiResponse<Void> response = this.analiticaFantasyApiClient.removeTierList(testContext.getAccessToken(), tierList.getId());

        validateStatusCodeResponse(response, 200, "TierList was not removed.");

        validateTextResponse("Tierlist eliminada con éxito", response, "TierList removed return a wrong message");
    }

    private TierListCreationRequestDTO buildTierListRequest(String id, String title, String description, List<TeamsDTO> teams, List<TierDTO> tiers, boolean ocultarNombres, boolean isAdmin) {
        TierListCreationRequestDTO request = new TierListCreationRequestDTO();
        request.setId(id);
        request.setTitle(title);
        request.setDescription(description);
        request.setTeams(teams);
        request.setTiers(tiers);
        request.setOcultarNombres(ocultarNombres);
        request.setAdmin(isAdmin);
        return request;
    }

    private List<TierDTO> defaultTierList() {
        return List.of(
                new TierDTO("Tier-1", "Apuestas TOP", betTopPlayers()),
                new TierDTO("Tier-2", "Buenas apuestas", bestBetPlayers()),
                new TierDTO("Tier-3", "Apuestas con riesgo", riskyBetPlayers()),
                new TierDTO("Tier-4", "Alinear solo como parches", alignOnlyAsPatchesPlayers()),
                new TierDTO("Tier-5", "No recomendables", notRecommendedPlayers()),
                new TierDTO("bolsa", "Bolsa", bagPlayers())
        );
    }

    private List<JugadorDTO> betTopPlayers() {
        return List.of(new JugadorDTO("129718", "Bellingham", "player"));
    }

    private List<JugadorDTO> bestBetPlayers() {
        return List.of(new JugadorDTO("756", "Valverde", "player"));
    }

    private List<JugadorDTO> riskyBetPlayers() {
        return List.of(new JugadorDTO("2207", "Camavinga", "player"));
    }

    private List<JugadorDTO> alignOnlyAsPatchesPlayers() {
        return List.of(new JugadorDTO("291964", "Arda Guler", "player"));
    }

    private List<JugadorDTO> notRecommendedPlayers() {
        return List.of(new JugadorDTO("377122", "Endrick", "player"));
    }

    private List<JugadorDTO> bagPlayers() {
        return List.of();
    }

    // Helper method
    @SuppressWarnings("unchecked")
    private <T> T getResponseData(Class<T> responseType) {
        return (T) testContext.getData().getBodyResponse();
    }

    private <T> void setTestContextData(ApiResponse<T> response) {
        ApiResponse<T> contextData = new ApiResponse<>();
        contextData.setBodyResponse(response.getBodyResponse());
        testContext.setData(contextData);
    }

    private void validateNotNullResponse(ApiResponse<?> response) {
        assertNotNull(response, "Response was null");
    }

    private void validateStatusCodeResponse(ApiResponse<?> response, int expectedStatus, String errorMessage) {
        assertEquals(expectedStatus, response.getStatusCode(), errorMessage);
    }

    private void validateTextResponse(String expectedMessage) {
        assertEquals(expectedMessage, this.testContext.getData().getTextResponse());
    }

    private void validateTextResponse(String expectedMessage, ApiResponse<?> response, String errorMessage) {
        assertEquals(expectedMessage, response.getTextResponse(), errorMessage);
    }



}
