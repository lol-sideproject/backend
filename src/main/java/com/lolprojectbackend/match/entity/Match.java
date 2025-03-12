package com.lolprojectbackend.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Match {

    @Id
    @Column(name = "match_id")
    private String id;

    private Long gameId;
    private String gameMode;
    private Long startTime;
    private Long playTime;
    private String gameType;

    @JsonIgnore
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();
}
