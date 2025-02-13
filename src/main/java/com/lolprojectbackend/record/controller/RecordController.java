package com.lolprojectbackend.record.controller;

import com.lolprojectbackend.record.dto.MatchDetailDto;
import com.lolprojectbackend.record.dto.MatchListDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import com.lolprojectbackend.record.service.RecordService;
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

}
