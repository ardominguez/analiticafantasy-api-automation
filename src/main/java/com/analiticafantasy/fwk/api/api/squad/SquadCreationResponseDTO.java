package com.analiticafantasy.fwk.api.api.squad;

import lombok.Data;

import java.util.List;

@Data
public class SquadCreationResponseDTO {

    private String id;
    private String name;
    private String formation;
    private List<PlayerDTO> players;
}
