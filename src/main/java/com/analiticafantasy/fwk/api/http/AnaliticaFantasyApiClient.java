package com.analiticafantasy.fwk.api.http;

import com.analiticafantasy.fwk.api.api.auth.AuthRequestDTO;
import com.analiticafantasy.fwk.api.api.auth.AuthResponseDTO;
import com.analiticafantasy.fwk.api.api.common.ApiResponse;
import com.analiticafantasy.fwk.api.api.squad.*;
import com.analiticafantasy.fwk.api.api.tierlist.TierListCreationRequestDTO;
import com.analiticafantasy.fwk.api.api.tierlist.TierListCreationResponseDTO;
import com.analiticafantasy.fwk.api.api.tierlist.TierListResponseDTO;
import com.analiticafantasy.fwk.api.api.tierlist.ViewTierListResponseDTO;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileUpdateRequestDTO;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileUpdateResponseDTO;
import com.analiticafantasy.fwk.api.api.userprofile.UserProfileResponseDTO;
import com.analiticafantasy.fwk.api.exception.HttpException;
import com.analiticafantasy.fwk.api.utils.JsonUtil;
import okhttp3.Response;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AnaliticaFantasyApiClient {

    private final HttpClient httpClient;

    private static final String LOGIN_PATH = "/accounts/login";
    private static final String REGISTER_PATH = "/register";
    private static final String USER_PROFILE_PATH = "/accounts/user-profile";
    private static final String FANTASY_SQUAD_PATH = "/fantasy-squad/%s";
    private static final String FANTASY_SQUADS_PATH = "/fantasy-squads";
    private static final String FANTASY_SQUADS_UPSERT_PATH = "/fantasy-squad/upsert";
    private static final String FANTASY_SHARE_SQUAD_PATH = "/fantasy-squad/share";
    private static final String FANTASY_SQUAD_SHARED_PATH = "/fantasy-squad/shared/%s";
    private static final String FANTASY_TIERLIST_PATH = "/tierlist";
    private static final String FANTASY_TIERLIST_DELETE_PATH = "/tierlist/%s";
    private static final String FANTASY_TIERLIST_GET_PATH = "/tierlist/ver-tierlist/%s";
    private static final String FANTASY_FLASH_NEWS_GET_PATH = "/noticias-flash-feed";


    public AnaliticaFantasyApiClient() {
        this.httpClient = new HttpClient();
    }

    public ApiResponse<AuthResponseDTO> loginUser(AuthRequestDTO authBody)  {
        return loginOrRegisterUser(LOGIN_PATH, authBody);
    }

    public ApiResponse<AuthResponseDTO> registerUser(AuthRequestDTO authBody)  {
        return loginOrRegisterUser(REGISTER_PATH, authBody);
    }

    private ApiResponse<AuthResponseDTO> loginOrRegisterUser(String servicePath, AuthRequestDTO authBody)  {
        return executePost(servicePath, authBody, AuthResponseDTO.class);
    }

    public ApiResponse<UserProfileUpdateResponseDTO> updateUserData(String token, UserProfileUpdateRequestDTO authBody)  {
        return executePut(token, USER_PROFILE_PATH, authBody, UserProfileUpdateResponseDTO.class);
    }

    public ApiResponse<UserProfileResponseDTO> getUserData(String token)  {
        return executeGet(token, USER_PROFILE_PATH, UserProfileResponseDTO.class);
    }

    public ApiResponse<SquadCreationResponseDTO> createSquad(String token, SquadCreationRequestDTO body)  {
        try (Response response = this.httpClient.post(token,"/fantasy-squad/upsert", body)) {

            ApiResponse<SquadCreationResponseDTO> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(response.code());

            String bodyStr = Objects.requireNonNull(response.body()).string();
            if (response.isSuccessful()) {
                if (JsonUtil.isValidJson(bodyStr)) {
                    apiResponse.setBodyResponse(JsonUtil.deserialize(bodyStr, SquadCreationResponseDTO.class));
                } else{
                    apiResponse.setTextResponse(bodyStr);
                }

            } else {
                apiResponse.setTextResponse(bodyStr);
            }
            return apiResponse;

        } catch (IOException ex) {
            throw new HttpException();
        }
    }

    public ApiResponse<SquadCreationResponseDTO> upsertSquad(String token, SquadUpdateRequestDTO body)  {
        return executePost(token, FANTASY_SQUADS_UPSERT_PATH, body, SquadCreationResponseDTO.class);
    }

    public ApiResponse<SquadResponseDTO> getSquad(String token, String squadId)  {
        String path = String.format(FANTASY_SQUAD_PATH, squadId);
        return executeGet(token, path, SquadResponseDTO.class);
    }

    public ApiResponse<SquadsResponseDTO> getSquads(String token)  {
        return executeGet(token, FANTASY_SQUADS_PATH, SquadsResponseDTO.class);
    }

    public ApiResponse<Void> removeSquad(String token, String squadId)  {
        String path = String.format(FANTASY_SQUAD_PATH, squadId);
        return executeDelete(token, path, Void.class);
    }

    public ApiResponse<ShareSquadCreationResponseDTO> shareSquad(String token, ShareSquadCreationRequestDTO body)  {
        return executePost(token, FANTASY_SHARE_SQUAD_PATH, body, ShareSquadCreationResponseDTO.class);
    }

    public ApiResponse<SharedSquadResponseDTO> sharedSquad(String token, String shareCode)  {
        String path = String.format(FANTASY_SQUAD_SHARED_PATH, shareCode);
        return executeGet(token, path, SharedSquadResponseDTO.class);
    }

    public ApiResponse<TierListCreationResponseDTO> upsertTierList(String token, TierListCreationRequestDTO body)  {
        return executePost(token, FANTASY_TIERLIST_PATH, body, TierListCreationResponseDTO.class);
    }

    public ApiResponse<TierListResponseDTO> getTierList(String token)  {
        return executeGet(token, FANTASY_TIERLIST_PATH, TierListResponseDTO.class);
    }

    public ApiResponse<Void> removeTierList(String token, String tierListId)  {
        String path = String.format(FANTASY_TIERLIST_DELETE_PATH, tierListId);
        return executeDelete(token, path, Void.class);
    }

    public ApiResponse<ViewTierListResponseDTO> getTierListInfo(String token, String slug)  {
        String path = String.format(FANTASY_TIERLIST_GET_PATH, slug);
        return executeGet(token, path, ViewTierListResponseDTO.class);
    }

    public ApiResponse<Document> getFlashNews(String token)  {
        return executeGet(token, FANTASY_FLASH_NEWS_GET_PATH, Document.class);
    }


    // Generic helper methods for handling HTTP requests
    private <T> ApiResponse<T> executePost(String servicePath, Object body, Class<T> responseType) {
        return executePost(null, servicePath, body, responseType);
    }

    private <T> ApiResponse<T> executePost(String token, String servicePath, Object body, Class<T> responseType) {
        try (Response response = this.httpClient.post(token, servicePath, body)) {
            return handleResponse(response, responseType);
        } catch (IOException ex) {
            throw new HttpException();
        }
    }

    private <T> ApiResponse<T> executePut(String token, String servicePath, Object body, Class<T> responseType) {
        try (Response response = this.httpClient.put(token, servicePath, body)) {
            return handleResponse(response, responseType);
        } catch (IOException ex) {
            throw new HttpException();
        }
    }

    private <T> ApiResponse<T> executeGet(String token, String servicePath, Class<T> responseType) {
        try (Response response = this.httpClient.get(token, servicePath)) {
            return handleResponse(response, responseType);
        } catch (IOException ex) {
            throw new HttpException();
        }
    }

    private <T> ApiResponse<T> executeDelete(String token, String servicePath, Class<T> responseType) {
        try (Response response = this.httpClient.delete(token, servicePath)) {
            return handleResponse(response, responseType);
        } catch (IOException ex) {
            throw new HttpException();
        }
    }

    /*private <T> ApiResponse<T> handleResponse(Response response, Class<T> responseType) throws IOException {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(response.code());

        String bodyStr = Objects.requireNonNull(response.body()).string();
        if (response.isSuccessful()) {
            if (JsonUtil.isValidJson(bodyStr)) {
                apiResponse.setBodyResponse(JsonUtil.deserialize(bodyStr, responseType));
            } else{
                apiResponse.setTextResponse(bodyStr);
            }
        } else {
            apiResponse.setTextResponse(bodyStr);
        }
        return apiResponse;
    }*/

    private <T> ApiResponse<T> handleResponse(Response response, Class<T> responseType) throws IOException {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(response.code());

        String bodyStr = Objects.requireNonNull(response.body()).string();
        if (response.isSuccessful()) {
            if (JsonUtil.isValidJson(bodyStr)) {
                apiResponse.setBodyResponse(JsonUtil.deserialize(bodyStr, responseType));
            } else if (isValidXml(bodyStr)) { // Nuevo chequeo para XML
                apiResponse.setXmlResponse(parseXml(bodyStr)); // Almacenar el XML
                } else {
                    apiResponse.setTextResponse(bodyStr);
                }
        } else {
            apiResponse.setTextResponse(bodyStr);
        }
        return apiResponse;
    }

    private boolean isValidXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (Exception e) {
            return false; // No es XML válido
        }
    }

    private Document parseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse XML response", e);
        }
    }


}
