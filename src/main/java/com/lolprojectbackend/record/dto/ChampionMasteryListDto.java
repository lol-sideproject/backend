package com.lolprojectbackend.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionMasteryListDto {
    private List<ChampionMasteryDto> topChampions;
}
