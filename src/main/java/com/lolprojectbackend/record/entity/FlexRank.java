package com.lolprojectbackend.record.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "flexrank")
public class FlexRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flexrank_id")
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
