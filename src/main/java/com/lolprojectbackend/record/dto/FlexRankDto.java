package com.lolprojectbackend.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FlexRankDto {
    private String tier;
    private String rankPosition;
    private Long leaguePoint;

}
