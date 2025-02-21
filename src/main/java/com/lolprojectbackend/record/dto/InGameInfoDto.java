package com.lolprojectbackend.record.dto;

import lombok.Data;
import java.util.List;

@Data
public class InGameInfoDto {
    private long gameId;
    private int mapId;
    private String gameMode;
    private String gameType;
    private long gameStartTime;
    private String platformId;
    private List<InGameParticipantDto> participants;
    private List<BannedChampionDto> bannedChampions;
}
