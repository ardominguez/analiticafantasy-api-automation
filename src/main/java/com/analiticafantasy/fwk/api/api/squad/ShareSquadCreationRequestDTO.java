package com.analiticafantasy.fwk.api.api.squad;

import lombok.Data;

@Data
public class ShareSquadCreationRequestDTO {

    private String id;
    private String name;
    private String formation;
    private PlayerDTO[] players;
}
