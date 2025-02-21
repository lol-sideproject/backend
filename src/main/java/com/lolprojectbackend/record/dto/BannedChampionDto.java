package com.lolprojectbackend.record.dto;

import lombok.Data;

@Data
public class BannedChampionDto {
    private int championId;
    private int teamId;
    private int pickTurn;
}
