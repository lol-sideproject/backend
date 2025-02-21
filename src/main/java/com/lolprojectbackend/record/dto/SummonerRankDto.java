package com.lolprojectbackend.record.dto;

import lombok.Data;

@Data
public class SummonerRankDto {
    private String id;         // encryptedSummonerId
    private String accountId;  // encryptedAccountId
    private String puuid;      // PUUID
    private String name;       // 소환사 닉네임
    private int summonerLevel; // 소환사 레벨
}
