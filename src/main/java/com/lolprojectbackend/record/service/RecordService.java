package com.lolprojectbackend.record.service;

import com.lolprojectbackend.record.dto.ChampionMasteryDto;
import com.lolprojectbackend.record.dto.ChampionMasteryListDto;
import com.lolprojectbackend.record.dto.FullRecordDto;
import com.lolprojectbackend.record.dto.InGameInfoDto;
import com.lolprojectbackend.record.dto.MatchDetailDto;
import com.lolprojectbackend.record.dto.MatchListDto;
import com.lolprojectbackend.record.dto.RankDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import com.lolprojectbackend.record.dto.SummonerRankDto;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

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
                .bodyToMono(SummonerDto.class); // JSON 응답을 DTO로 변환
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
        return Mono.delay(Duration.ofMillis(200)) // 200ms 딜레이 추가
                .then(webClient.get()
                        .uri("/lol/match/v5/matches/{matchId}", matchId)
                        .header("X-Riot-Token", riotApiKey)
                        .retrieve()
                        .bodyToMono(MatchDetailDto.class)
                        .doOnNext(response -> System.out.println("Match Detail Loaded: " + response))
                );
    }


    public Mono<FullRecordDto> getFullRecord(String gameName, String tagLine) {
        AtomicInteger requestCount = new AtomicInteger(0); // 요청 개수 카운트

        return getPuuid(gameName, tagLine) // 닉네임 + 태그로 PUUID 조회
                .flatMap(summoner -> getRecentMatches(summoner.getPuuid()) // 최근 20경기 ID 리스트 조회
                        .flatMapMany(matchListDto -> Flux.fromIterable(matchListDto.getMatchIds())) // 각 경기 ID로 상세 정보 조회
                        .index()
                        .flatMap(tuple -> {
                            long index = tuple.getT1();
                            String matchId = tuple.getT2();

                            // 100번째 요청마다 2분 대기 (2분당 100개 요청 제한)
                            if (requestCount.incrementAndGet() % 100 == 0) {
                                System.out.println("100개 요청 완료, 2분 대기");
                                return Mono.delay(Duration.ofMinutes(2)).then(getMatchDetailWithRetry(matchId));
                            }

                            return Mono.delay(Duration.ofMillis(50)) //  50ms 간격으로 요청 (1초당 20개 제한)
                                    .then(getMatchDetailWithRetry(matchId));
                        })
                        .collectList()
                        .map(matchDetails -> {
                            // 최종 응답 객체 생성
                            FullRecordDto fullRecordDto = new FullRecordDto();
                            fullRecordDto.setSummoner(summoner);
                            fullRecordDto.setMatches(matchDetails);
                            return fullRecordDto;
                        }));
    }

    public Mono<MatchDetailDto> getMatchDetailWithRetry(String matchId) {
        return webClient.get()
                .uri("/lol/match/v5/matches/{matchId}", matchId)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == 429, clientResponse -> {
                    // Riot API의 응답 헤더에서 "Retry-After" 값을 가져오기
                    int retryAfter = clientResponse.headers().header("Retry-After")
                            .stream().findFirst().map(Integer::parseInt).orElse(5); // 기본 5초 대기

                    System.out.println("429 Too Many Requests - Retrying after " + retryAfter + " seconds...");
                    return Mono.delay(Duration.ofSeconds(retryAfter)).then(Mono.empty());
                })
                .bodyToMono(MatchDetailDto.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))) // 5초 간격으로 최대 3번 재시도
                .doOnNext(response -> System.out.println("Match Detail Loaded: " + response));
    }

    /**
     * PUUID를 사용하여 챔피언 숙련도 높은 3개 반환
     */
    public Mono<ChampionMasteryListDto> getTopChampions(String puuid) {
        return webClient.get()
                .uri("https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}/top?count=3", puuid)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .onStatus(status -> status.value() == 403, response ->
                        Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "API 권한이 없습니다. API 키를 확인하세요."))
                )
                .onStatus(status -> status.value() == 404, response ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "소환사 정보를 찾을 수 없습니다."))
                )
                .bodyToMono(ChampionMasteryDto[].class)
                .map(championMasteries -> new ChampionMasteryListDto(
                        championMasteries != null ? Arrays.asList(championMasteries) : List.of()
                ));
    }

    /**
     * PUUID로 encryptedSummonerId 조회
     */
    public Mono<String> getSummonerIdByPuuid(String puuid) {
        return webClient.get()
                .uri("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{puuid}", puuid)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(SummonerRankDto.class)
                .map(SummonerRankDto::getId) // encryptedSummonerId 반환
                .doOnSuccess(id -> log.info("Retrieved encryptedSummonerId: {}", id))
                .doOnError(error -> log.error("Failed to get encryptedSummonerId for PUUID: {}", puuid, error));
    }

    /**
     * encryptedSummonerId로 랭크 조회
     */
    public Mono<List<RankDto>> getRankBySummonerId(String encryptedSummonerId) {
        return webClient.get()
                .uri("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}", encryptedSummonerId)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RankDto>>() {})
                .doOnSuccess(rankList -> log.info("Retrieved Rank Info: {}", rankList))
                .doOnError(error -> log.error("Failed to fetch rank for SummonerId: {}", encryptedSummonerId, error));
    }


    /**
     * encryptedSummonerId로 현재 인게임 정보 조회
     */
    public Mono<InGameInfoDto> getInGameInfo(String encryptedSummonerId) {
        return webClient.get()
                .uri("https://kr.api.riotgames.com/lol/spectator/v5/active-games/by-summoner/{encryptedSummonerId}", encryptedSummonerId)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                    log.info("사용자가 현재 게임 중이 아닙니다. (404 Not Found)");
                    return Mono.empty(); // 404일 경우 빈 응답 처리
                })
                .bodyToMono(InGameInfoDto.class);
    }

}
