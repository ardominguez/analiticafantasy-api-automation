package com.analiticafantasy.fwk.api.api.tierlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TierListResponseDTO {

    private List<TierListInfoDTO> tierlists;
    private boolean isCached;
    private AuthorsDTO[] authors;
}
