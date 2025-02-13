package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchDetailDto {
    private MetadataDto metadata;
    private InfoDto info;
}
