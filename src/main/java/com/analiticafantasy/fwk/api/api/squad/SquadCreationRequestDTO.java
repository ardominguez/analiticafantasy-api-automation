package com.analiticafantasy.fwk.api.api.squad;

import lombok.Data;

@Data
public class SquadCreationRequestDTO {

    private String name;
    private String formation;
    private PlayerDTO[] players;
    private String version;

}
