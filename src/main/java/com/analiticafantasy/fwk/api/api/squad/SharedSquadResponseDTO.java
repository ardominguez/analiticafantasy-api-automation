package com.analiticafantasy.fwk.api.api.squad;

import lombok.Data;

@Data
public class SharedSquadResponseDTO {

    private String id;
    private String created;
    private String name;
    private String formation;
    private PlayerDTO[] players;

}
