package com.lolprojectbackend.record.controller;

import com.lolprojectbackend.record.dto.ChampionMasteryListDto;
import com.lolprojectbackend.record.dto.InGameInfoDto;
import com.lolprojectbackend.record.dto.RankDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import com.lolprojectbackend.record.dto.SummonerInfoDto;
import com.lolprojectbackend.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final RecordService recordService;

    /**
     * 소환사 puuid조회
     */
    @GetMapping("/puuid/{gameName}/{tagLine}")
    public ResponseEntity<SummonerDto> getPuuid(@PathVariable String gameName, @PathVariable String tagLine) {
        return ResponseEntity.ok(recordService.getPuuid(gameName, tagLine));
    }

    /**
     * 소환사의 모스트 챔피언 3개 조회 API
     */
    @GetMapping("/top-champions/{puuid}")
    public ResponseEntity<ChampionMasteryListDto> getTopChampions(@PathVariable String puuid) {
        return ResponseEntity.ok(recordService.getTopChampions(puuid));
    }

    /**
     * PUUID로 Summoner 정보 조회 API
     */
    @GetMapping("/summoner/by-puuid/{puuid}")
    public ResponseEntity<SummonerInfoDto> getSummonerByPuuid(@PathVariable String puuid) {
        SummonerInfoDto summoner = recordService.getSummonerByPuuid(puuid);
        return summoner != null ? ResponseEntity.ok(summoner) : ResponseEntity.notFound().build();
    }

    /**
     * encryptedSummonerId로 랭크(자유랭크, 솔로랭크) 조회 API
     */
    @GetMapping("/rank/{encryptedSummonerId}")
    public ResponseEntity<List<RankDto>> getRankBySummonerId(@PathVariable String encryptedSummonerId) {
        return ResponseEntity.ok(recordService.getRankBySummonerId(encryptedSummonerId));
    }

    /**
     * PUUID로 현재 인게임 정보 조회
     */
    @GetMapping("/ingame/{puuid}")
    public ResponseEntity<InGameInfoDto> getInGameInfo(@PathVariable String puuid) {
        InGameInfoDto inGameInfo = recordService.getInGameInfo(puuid);
        return inGameInfo != null ? ResponseEntity.ok(inGameInfo) : ResponseEntity.notFound().build();
    }


}
