package com.analiticafantasy.fwk.api.api.squad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SquadDTO {
    private String id;
    private String created;
    private String name;
    private List<PlayerDTO> players;

}
