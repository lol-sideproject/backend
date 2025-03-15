package com.lolprojectbackend.record.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "solorank")
public class SoloRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "soloRank_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "summoner_id", nullable = false)
    private Summoner summoner;

    @Column(name = "tier")
    private String tier;

    @Column(name = "rank_position")
    private String rankPosition;

    @Column(name = "league_point")
    private Long leaguePoint;
}
