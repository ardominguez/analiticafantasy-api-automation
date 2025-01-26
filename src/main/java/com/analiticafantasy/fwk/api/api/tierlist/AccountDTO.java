package com.analiticafantasy.fwk.api.api.tierlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String nickname;
    private String image;
    private UserSocialDTO userSocial;
}
