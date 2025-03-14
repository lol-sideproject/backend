package com.lolprojectbackend.record.repository;

import com.lolprojectbackend.record.entity.SoloRank;
import com.lolprojectbackend.record.entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoloRankRepository extends JpaRepository<SoloRank, Long> {
    // 특정 소환사의 솔로랭크 정보 조회
    List<SoloRank> findBySummoner(Summoner summoner);
}

