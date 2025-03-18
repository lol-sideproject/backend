package com.lolprojectbackend.record.dto;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SummonerDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Long level;
    private Long icon;
    private Date renewTime;

    private SoloRankDto soloRank;
    private FlexRankDto flexRank;
}