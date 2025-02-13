package com.lolprojectbackend.record.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TeamDto {
    private int teamId;
    private boolean win;
    private List<BanDto> bans;
    private ObjectivesDto objectives;
}
