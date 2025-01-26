package com.analiticafantasy.fwk.api.api.tierlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TierDTO {

    private String id;
    private String name;
    private List<JugadorDTO> jugadores;


}
