package com.analiticafantasy.fwk.api.api.tierlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewTierListResponseDTO {

    private String created;
    private String title;
    private String description;
    private String slug;
    private List<TierDTO> tiers;
    private boolean ocultarNombres;
    private String id;
    private boolean isAdmin;
    AccountDTO account;



}
