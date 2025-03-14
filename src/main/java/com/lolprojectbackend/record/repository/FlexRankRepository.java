package com.lolprojectbackend.record.repository;

import com.lolprojectbackend.record.entity.FlexRank;
import com.lolprojectbackend.record.entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlexRankRepository extends JpaRepository<FlexRank, Long> {
    // 특정 소환사의 자유랭크 정보 조회
    List<FlexRank> findBySummoner(Summoner summoner);
}
