package com.analiticafantasy.fwk.api.api.userprofile;

import lombok.Data;

@Data
public class UserProfileResponseDTO {
    private String username;
    private String nickname;
    private Object userSocial;
    private boolean isEmailConfirmed;
}
