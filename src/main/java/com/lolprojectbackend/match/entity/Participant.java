package com.lolprojectbackend.match.entity;

import com.lolprojectbackend.record.entity.Summoner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Participant {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    private int kills;
    private int assistants;
    private Long totalDamageTaken;
    private Long totalDamageDealt;
    private String champion;
    private boolean isWin;
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;
    private int summoner1Id;
    private int summoner2Id;
    private String role;
    private Long goldEarned;
}
