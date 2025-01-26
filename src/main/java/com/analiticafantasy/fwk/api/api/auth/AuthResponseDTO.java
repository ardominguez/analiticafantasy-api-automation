package com.analiticafantasy.fwk.api.api.auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String username;
    private String nickname;
    private String role;
    private String email;
    private String subscriptionType;
    private String isAdmin;
    private String logged;
    private String isSecurity;
    private String version;
}
