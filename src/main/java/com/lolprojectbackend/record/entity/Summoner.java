package com.lolprojectbackend.record.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SUMMONER")
public class Summoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUMMONER_ID")
    private Long summonerId;

    @Column(name = "PUUID", unique = true, nullable = false)
    private String puuid;

    @Column(name = "SUMMONER_NAME", nullable = false)
    private String summonerName;

    @Column(name = "SUMMONER_TAG", nullable = false)
    private String summonerTag;

    @Column(name = "LEVEL")
    private Long level;

    @Column(name = "ICON")
    private Long icon;
}
