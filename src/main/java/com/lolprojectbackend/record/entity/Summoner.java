package com.lolprojectbackend.record.entity;

import jakarta.persistence.*;
import java.sql.Date;
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
    private Long id;

    @Column(name = "PUUID", unique = true, nullable = false)
    private String puuid;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TAG", nullable = false)
    private String tag;

    @Column(name = "LEVEL")
    private Long level;

    @Column(name = "ICON")
    private Long icon;

    @Column(name = "RENEW_TIME")
    private Date renewTime;
}
