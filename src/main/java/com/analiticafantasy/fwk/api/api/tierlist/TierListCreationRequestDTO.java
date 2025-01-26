package com.analiticafantasy.fwk.api.api.tierlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TierListCreationRequestDTO {

    private String id;
    private String title;
    private String description;
    private List<TeamsDTO> teams;
    private List<TierDTO> tiers;
    private boolean ocultarNombres;
    private boolean isAdmin;

}
