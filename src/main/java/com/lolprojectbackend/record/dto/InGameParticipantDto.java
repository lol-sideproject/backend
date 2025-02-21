package com.lolprojectbackend.record.dto;

import lombok.Data;

@Data
public class InGameParticipantDto {
    private int teamId;
    private String summonerName;
    private String summonerId;
    private int championId;
    private int spell1Id;
    private int spell2Id;
}
