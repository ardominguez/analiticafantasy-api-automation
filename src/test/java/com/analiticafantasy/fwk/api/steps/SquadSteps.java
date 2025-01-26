package com.analiticafantasy.fwk.api.steps;

import com.analiticafantasy.fwk.api.BaseStepDefinition;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.api.squad.*;
import com.analiticafantasy.fwk.api.hook.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadSteps extends BaseStepDefinition {

    private final TestContext<ApiResponse<?>> testContext;

    public SquadSteps(TestContext<ApiResponse<?>> testContext) {
        this.testContext = testContext;
    }

    @When("User creates a fantasy squad")
    public void createFantasySquad() {
        SquadCreationRequestDTO squadRequest = buildSquadRequest("nameTest", "alineacion_3-3-4", defaultPlayers());
        ApiResponse<SquadCreationResponseDTO> response = analiticaFantasyApiClient.createSquad(testContext.getAccessToken(), squadRequest);

        assertEquals(200, response.getStatusCode(), "Squad creation failed.");
        testContext.setData(response);
    }

    @And("Squad is created properly")
    public void validateSquadCreation() {
        SquadCreationResponseDTO createdSquad = getResponseData(SquadCreationResponseDTO.class);
        ApiResponse<SquadResponseDTO> response = analiticaFantasyApiClient.getSquad(testContext.getAccessToken(), createdSquad.getId());

        SquadResponseDTO squadResponseDTO = response.getBodyResponse();
        assertNotNull(squadResponseDTO);

        assertEquals(200, response.getStatusCode(), "Failed to retrieve created squad.");
        assertEquals("nameTest", response.getBodyResponse().getSquad().getName(), "Squad name mismatch.");
    }

    @And("User removes the created squad")
    public void removeCreatedSquad() {
        String squadId = getCreatedSquadId();
        ApiResponse<Void> response = analiticaFantasyApiClient.removeSquad(testContext.getAccessToken(), squadId);

        assertEquals(200, response.getStatusCode(), "Failed to remove squad.");
        assertEquals("Fantasy squad deleted", response.getTextResponse(), "Unexpected delete message.");
    }


    @Then("Squad was removed properly")
    public void validateSquadRemoval() {
        String squadId = getCreatedSquadId();
        ApiResponse<SquadsResponseDTO> response = analiticaFantasyApiClient.getSquads(testContext.getAccessToken());

        assertEquals(200, response.getStatusCode(), "Failed to retrieve squads.");
        assertFalse(response.getBodyResponse().getSquads().stream().anyMatch(squad -> squad.getId().equals(squadId)),
                "Squad still exists after deletion.");
    }

    @And("I retrieve a fantasy squad {string}")
    public void userGetAFantasySquadToUpdated(String squadName) {

        ApiResponse<SquadsResponseDTO> apiResponse = this.analiticaFantasyApiClient.getSquads(this.testContext.getAccessToken());

        assertNotNull(apiResponse.getBodyResponse());
        assertEquals(200, apiResponse.getStatusCode());

        var squadList = apiResponse.getBodyResponse().getSquads();

        var squadOpt = squadList.stream().filter(sq -> sq.getName().equals(squadName)).findFirst();
        assertTrue(squadOpt.isPresent(), "Squad not found");

        var squad = squadOpt.get();

        ApiResponse<SquadDTO>  squadContext = new ApiResponse<>();
        squadContext.setBodyResponse(squad);

        testContext.setData(squadContext);
    }

    @When("User adds new players to the fantasy squad {string}")
    public void addPlayersToSquad(String squadName) {
        SquadDTO squad = getResponseData(SquadDTO.class);
        SquadUpdateRequestDTO updateRequest = buildSquadUpdateRequest(squad.getId(), squadName, "alineacion_3-3-4", additionalPlayers());

        ApiResponse<SquadCreationResponseDTO> response = analiticaFantasyApiClient.upsertSquad(testContext.getAccessToken(), updateRequest);
        assertEquals(200, response.getStatusCode(), "Failed to update squad.");
        testContext.setData(response);
    }

    @When("User removes players from the squad")
    public void removePlayersFromSquad() {
        SquadDTO squad = getResponseData(SquadDTO.class);
        SquadUpdateRequestDTO updateRequest = buildSquadUpdateRequest(squad.getId(), squad.getName(), "alineacion_3-3-4", new PlayerDTO[]{});

        ApiResponse<SquadCreationResponseDTO> response = analiticaFantasyApiClient.upsertSquad(testContext.getAccessToken(), updateRequest);
        assertEquals(200, response.getStatusCode(), "Failed to update squad.");
        testContext.setData(response);
    }

    @Then("Squad {string} is updated without errors")
    public void squadIsUpdatedWithoutErrors(String squadName) {

        SquadCreationResponseDTO updatedSquad = getResponseData(SquadCreationResponseDTO.class);
        assertNotNull(updatedSquad);

        List<PlayerDTO> playerList = updatedSquad.getPlayers();

        var apiResponse = this.analiticaFantasyApiClient.getSquad(this.testContext.getAccessToken(), updatedSquad.getId());
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals(squadName, apiResponse.getBodyResponse().getSquad().getName());
        assertEquals(playerList.size(), apiResponse.getBodyResponse().getSquad().getPlayers().size());

        List<PlayerDTO> playerListAdded = apiResponse.getBodyResponse().getSquad().getPlayers();

        assertTrue(playerListAdded.containsAll(playerList));

        ApiResponse<SquadDTO>  squadContext = new ApiResponse<>();
        squadContext.setBodyResponse(apiResponse.getBodyResponse().getSquad());
        testContext.setData(squadContext);
    }

    @When("I send a POST request to share a squad")
    public void iSendAPostRequestToShareASquad() {

        var squadDTO = getResponseData(SquadDTO.class);

        var request = buildShareSquadUpdateRequest(squadDTO.getId(), "Ailen2", "alineacion_3-3-4", sharePlayers());
        var apiResponse = this.analiticaFantasyApiClient.shareSquad(this.testContext.getAccessToken(), request);

        assertEquals(200, apiResponse.getStatusCode());
        testContext.setData(apiResponse);
    }

    @When("the squad link was generated successfully")
    public void theSquadLinkWasGeneratedSuccessfully() {
        var response = getResponseData(ShareSquadCreationResponseDTO.class);
        String shareCode = response.getShareCode();

        String url = String.format("https://www.analiticafantasy.com/compartir-plantilla/%s", shareCode);

        Response pageResponse = RestAssured.get(url);

        assertEquals(200, pageResponse.getStatusCode());
    }

    @When("I send a GET request to get the squad shared")
    public void validateSquadShared() {

        var response = getResponseData(ShareSquadCreationResponseDTO.class);
        var apiResponse = this.analiticaFantasyApiClient.sharedSquad(this.testContext.getAccessToken(), response.getShareCode());

        assertEquals(200, apiResponse.getStatusCode());
        assertNotNull(apiResponse.getBodyResponse());
        assertNotNull(apiResponse.getBodyResponse().getPlayers());

        testContext.setData(apiResponse);
    }

    @Then("the squad is retrieved successfully")
    public void theSquadIsRetrievedSuccessfully() {

        var response = getResponseData(SharedSquadResponseDTO.class);

        assertEquals("Ailen2", response.getName(), "Squad name is wrong");
        assertEquals("a9ff91e4-18dd-4360-a95f-361076eb5179", response.getId(), "Squad id is wrong");
        assertEquals("alineacion_3-3-4", response.getFormation(), "Squad formation is wrong");
    }

    // Helper methods
    private PlayerDTO[] defaultPlayers() {
        return new PlayerDTO[]{
                new PlayerDTO(47543, 43), new PlayerDTO(184226, 44), new PlayerDTO(30510, 41),
                new PlayerDTO(47445, 42), new PlayerDTO(336594, 33), new PlayerDTO(47309, 32),
                new PlayerDTO(332308, 31), new PlayerDTO(70500, 23), new PlayerDTO(48372, 22),
                new PlayerDTO(101814, 21), new PlayerDTO(47270, 11)
        };
    }

    private PlayerDTO[] sharePlayers() {
        return new PlayerDTO[]{
                new PlayerDTO(47543, 42), new PlayerDTO(67955, 43), new PlayerDTO(30510, 41),
                new PlayerDTO(47393, 44), new PlayerDTO(47320, 72), new PlayerDTO(53, 71), new PlayerDTO(83, 74), new PlayerDTO(521, 73),
                new PlayerDTO(41552, 32), new PlayerDTO(2472, 31), new PlayerDTO(2278, 22),
                new PlayerDTO(48372, 23), new PlayerDTO(18895, 21), new PlayerDTO(26, 11)
        };
    }

    private PlayerDTO[] additionalPlayers() {
        return new PlayerDTO[]{
                new PlayerDTO(184226, 95), new PlayerDTO(18895, 23), new PlayerDTO(47273, 22),
                new PlayerDTO(70500, 21), new PlayerDTO(126, 11)
        };
    }

    private SquadCreationRequestDTO buildSquadRequest(String name, String formation, PlayerDTO[] players) {
        SquadCreationRequestDTO request = new SquadCreationRequestDTO();
        request.setName(name);
        request.setFormation(formation);
        request.setPlayers(players);
        return request;
    }

    private SquadUpdateRequestDTO buildSquadUpdateRequest(String id, String name, String formation, PlayerDTO[] players) {
        SquadUpdateRequestDTO request = new SquadUpdateRequestDTO();
        request.setId(id);
        request.setName(name);
        request.setFormation(formation);
        request.setPlayers(players);
        request.setVersion(1);
        return request;
    }

    private ShareSquadCreationRequestDTO buildShareSquadUpdateRequest(String id, String name, String formation, PlayerDTO[] players) {
        ShareSquadCreationRequestDTO request = new ShareSquadCreationRequestDTO();
        request.setId(id);
        request.setName(name);
        request.setFormation(formation);
        request.setPlayers(players);
        return request;
    }

    private String getCreatedSquadId() {
        SquadCreationResponseDTO response = getResponseData(SquadCreationResponseDTO.class);
        return response.getId();
    }

    @SuppressWarnings("unchecked")
    private <T> T getResponseData(Class<T> responseType) {
        return (T) testContext.getData().getBodyResponse();
    }


}
