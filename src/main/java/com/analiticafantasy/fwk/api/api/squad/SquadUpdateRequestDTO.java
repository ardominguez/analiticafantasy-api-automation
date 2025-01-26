package com.analiticafantasy.fwk.api.api.squad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SquadUpdateRequestDTO {

    private String created;
    private String formation;
    private String id;
    private String name;
    private PlayerDTO[] players;
    private int version;

}
