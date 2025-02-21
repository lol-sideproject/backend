package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FullRecordDto {
    private SummonerDto summoner;
    private List<MatchDetailDto> matches;
}
