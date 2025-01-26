package com.analiticafantasy.fwk.api.http;

import com.analiticafantasy.fwk.api.utils.ConfigurationsLoader;
import com.analiticafantasy.fwk.api.utils.JsonUtil;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.commons.collections4.MapUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.analiticafantasy.fwk.api.exception.HttpException;

public class HttpClient {

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final String baseUrl;
    private final OkHttpClient client;

    public HttpClient() {
        OkHttpClient.Builder clientBuilder = new  OkHttpClient.Builder();
        clientBuilder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS);

        this.client = clientBuilder.build();
        this.baseUrl = ConfigurationsLoader.getConfigValue("analiticafantasy.baseUrl");
    }

    public Response get(String token, String servicePath) {
        try {
            Call call = buildCall(servicePath,null, RequestType.GET, buildAuthorizationHeader(token));
            return call.execute();
        } catch (Exception ex) {
            throw new HttpException();
        }
    }

    public Response post(String servicePath, Object body) {
        return post(null, servicePath, body);
    }

    public Response post(String token, String servicePath, Object body) {
        String json = JsonUtil.serialize(body);
        RequestBody requestBody;
        if(body instanceof FormBody){
            requestBody = (RequestBody) body;
        } else {
            requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);
        }

        try {
            Call call = buildCall(servicePath, requestBody, RequestType.POST, buildAuthorizationHeader(token));
            return call.execute();
        } catch (Exception ex) {
            throw new HttpException();
        }
    }

    public Response put(String token, String servicePath, Object body) {
        String json = JsonUtil.serialize(body);
        RequestBody requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);

        try {
            Call call = buildCall(servicePath, requestBody, RequestType.PUT, buildAuthorizationHeader(token));
            return call.execute();
        } catch (Exception ex) {
            throw new HttpException();
        }
    }

    public Response patch(String token, String servicePath, Object body) {
        String json = JsonUtil.serialize(body);
        RequestBody requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);

        try {
            Call call = buildCall(servicePath, requestBody, RequestType.PATCH, buildAuthorizationHeader(token));
            return call.execute();
        } catch (Exception ex) {
            throw new HttpException();
        }
    }

    public Response delete(String token, String servicePath) {
        try {
            Call call = buildCall(servicePath,null, RequestType.DELETE, buildAuthorizationHeader(token));
            return call.execute();
        } catch (Exception ex) {
            throw new HttpException();
        }
    }

    private Call buildCall(String servicePath, Object body, RequestType type, Map<String, String> headers) {
        RequestBody requestBody = null;

        String url = String.format("%s%s", baseUrl, servicePath);
        final Builder builder = new Request.Builder().url(url);

        if (!type.isGet()) {
            if (body != null) {
                if (body instanceof RequestBody) {
                    requestBody = (RequestBody) body;
                } else {
                    String json = JsonUtil.serialize(body);
                    requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);
                }

            }

            switch (type) {
                case POST:
                    builder.post(requestBody);
                    break;
                case PUT:
                    builder.put(requestBody);
                    break;
                case PATCH:
                    builder.patch(requestBody);
                    break;
                case DELETE:
                    if (requestBody !=null) {
                        builder.delete(requestBody);
                    }else {
                        builder.delete();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported request type for body: " + type);
            }
        } else {
            builder.get();
        }

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(builder::addHeader);
        }
        return this.client.newCall(builder.build());
    }

    private Map<String, String> buildAuthorizationHeader(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return new HashMap<>();
        }
        return Map.of("Authorization", String.format("Bearer %s", accessToken));
    }

}
