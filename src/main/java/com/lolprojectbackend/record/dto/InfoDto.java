package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfoDto {
    private String endOfGameResult;
    private long gameCreation;
    private long gameDuration;
    private long gameEndTimestamp;
    private long gameId;
    private String gameMode;
    private String gameName;
    private long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int mapId;
    private String platformId;
    private int queueId;
    private String tournamentCode;

    private List<MatchParticipantDto> participants;
    private List<TeamDto> teams;
}
