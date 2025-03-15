package com.lolprojectbackend.record.repository;

import com.lolprojectbackend.record.entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SummonerRepository extends JpaRepository<Summoner, Long> {
    Optional<Summoner> findByPuuid(String puuid);  // PUUID로 소환사 조회
}
