package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonerInfoDto {
    private String id;  // encryptedSummonerId
    private String accountId;
    private String puuid;
    private int profileIconId;
    private long revisionDate;
    private int summonerLevel;
}
