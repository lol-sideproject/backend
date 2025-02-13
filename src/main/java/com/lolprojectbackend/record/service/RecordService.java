package com.lolprojectbackend.record.service;

import com.lolprojectbackend.record.dto.MatchDetailDto;
import com.lolprojectbackend.record.dto.MatchListDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final WebClient webClient;

    @Value("${riot.api.key}") // application.yml에서 Riot API Key 가져오기
    private String riotApiKey;

    public Mono<SummonerDto> getPuuid(String gameName, String tagLine) {
        return webClient.get()
                .uri("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}", gameName, tagLine)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(SummonerDto.class); // ✅ JSON 응답을 DTO로 변환
    }

    public Mono<MatchListDto> getRecentMatches(String puuid) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lol/match/v5/matches/by-puuid/{puuid}/ids")
                        .queryParam("start", 0)  // 최신 경기부터 시작
                        .queryParam("count", 20) // 최대 20경기 가져오기
                        .build(puuid))
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {}) // JSON 배열을 List로 변환
                .map(MatchListDto::new); // DTO로 매핑
    }

    /**
     * 개별 경기 상세 정보 조회
     */
    public Mono<MatchDetailDto> getMatchDetail(String matchId) {
        return webClient.get()
                .uri("/lol/match/v5/matches/{matchId}", matchId)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(MatchDetailDto.class) // ✅ Riot API JSON을 DTO로 변환
                .doOnNext(response -> System.out.println("🔥 Converted DTO: " + response));
    }

}
