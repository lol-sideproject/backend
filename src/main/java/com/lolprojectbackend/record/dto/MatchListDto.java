package com.lolprojectbackend.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MatchListDto {

    private List<String> matchIds;

}
