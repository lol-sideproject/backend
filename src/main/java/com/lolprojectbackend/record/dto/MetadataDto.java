package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetadataDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants; // 참가자의 PUUID 리스트
}
