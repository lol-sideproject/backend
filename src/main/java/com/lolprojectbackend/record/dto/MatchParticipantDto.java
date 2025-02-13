package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchParticipantDto {
    private String puuid;
    private String riotIdGameName;
    private String riotIdTagline;
    private String summonerName;
    private int summonerLevel;
    private String championName;
    private int kills;
    private int deaths;
    private int assists;
    private boolean win;
    private int totalDamageDealt;
    private int totalDamageTaken;
    private int visionScore;
    private int goldEarned;
    private int totalMinionsKilled;
}
