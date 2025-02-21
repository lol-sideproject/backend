package com.lolprojectbackend.record.controller;

import com.lolprojectbackend.record.dto.ChampionMasteryListDto;
import com.lolprojectbackend.record.dto.FullRecordDto;
import com.lolprojectbackend.record.dto.InGameInfoDto;
import com.lolprojectbackend.record.dto.MatchDetailDto;
import com.lolprojectbackend.record.dto.MatchListDto;
import com.lolprojectbackend.record.dto.RankDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import com.lolprojectbackend.record.service.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/puuid/{gameName}/{tagLine}")
    public Mono<ResponseEntity<SummonerDto>> getPuuid(@PathVariable String gameName, @PathVariable String tagLine) {
        return recordService.getPuuid(gameName, tagLine)
                .map(ResponseEntity::ok); // DTO를 JSON 응답으로 반환
    }

    /**
     * 최근 20경기 ID 리스트 조회 API
     */
    @GetMapping("/matches/{puuid}")
    public Mono<ResponseEntity<MatchListDto>> getRecentMatches(@PathVariable String puuid) {
        return recordService.getRecentMatches(puuid)
                .map(ResponseEntity::ok);
    }



    /**
     * 개별 경기 상세 정보 조회 API
     */
    @GetMapping("/match/{matchId}")
    public Mono<ResponseEntity<MatchDetailDto>> getMatchDetail(@PathVariable String matchId) {
        return recordService.getMatchDetail(matchId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/full/{gameName}/{tagLine}")
    public Mono<ResponseEntity<FullRecordDto>> getFullRecord(@PathVariable String gameName, @PathVariable String tagLine) {
        return recordService.getFullRecord(gameName, tagLine)
                .map(ResponseEntity::ok);
    }

    /**
     * 소환사의 모스트 챔피언 3개 조회 API
     */
    @GetMapping("/top-champions/{puuid}")
    public Mono<ResponseEntity<ChampionMasteryListDto>> getTopChampions(@PathVariable String puuid) {
        return recordService.getTopChampions(puuid)
                .map(ResponseEntity::ok);
    }

    /**
     * PUUID로 encryptedSummonerId 조회 API
     */
    @GetMapping("/summonerId/{puuid}")
    public Mono<ResponseEntity<String>> getSummonerIdByPuuid(@PathVariable String puuid) {
        return recordService.getSummonerIdByPuuid(puuid)
                .map(ResponseEntity::ok);
    }

    /**
     * encryptedSummonerId로 랭크 조회 API
     */
    @GetMapping("/rank/{encryptedSummonerId}")
    public Mono<ResponseEntity<List<RankDto>>> getRankBySummonerId(@PathVariable String encryptedSummonerId) {
        return recordService.getRankBySummonerId(encryptedSummonerId)
                .map(ResponseEntity::ok);
    }

    /**
     * PUUID로 현재 인게임 정보 조회
     */
    @GetMapping("/ingame/{puuid}")
    public Mono<ResponseEntity<InGameInfoDto>> getInGameInfo(@PathVariable String puuid) {
        return recordService.getInGameInfo(puuid)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build()); // 게임 중이 아닐 경우 404 반환
    }




}
