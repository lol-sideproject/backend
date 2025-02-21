package com.lolprojectbackend.record.dto;

import lombok.Data;

@Data
public class RankDto {
    private String leagueId;
    private String queueType;    // RANKED_SOLO_5x5, RANKED_FLEX_SR 등
    private String tier;         // GOLD, PLATINUM 등
    private String rank;         // I, II, III 등
    private String summonerId;   // encryptedSummonerId
    private String summonerName; // 소환사 닉네임
    private int leaguePoints;
    private int wins;
    private int losses;
}
