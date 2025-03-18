package com.lolprojectbackend.record.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "solorank")  // 테이블명 확인
public class SoloRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solorank_id")  // 정확한 컬럼명 사용
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

