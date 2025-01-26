package com.analiticafantasy.fwk.api.api.userprofile;

import lombok.Data;

@Data
public class UserProfileUpdateRequestDTO {
    private String username;
    private String nickname;
    private Object userSocial;
}
