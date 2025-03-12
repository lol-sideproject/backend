package com.lolprojectbackend.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lolprojectbackend.match.entity.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();
}
