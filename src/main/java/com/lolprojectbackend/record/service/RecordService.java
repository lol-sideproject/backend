package com.lolprojectbackend.record.service;

import com.lolprojectbackend.record.dto.ChampionMasteryDto;
import com.lolprojectbackend.record.dto.ChampionMasteryListDto;
import com.lolprojectbackend.record.dto.InGameInfoDto;
import com.lolprojectbackend.record.dto.RankDto;
import com.lolprojectbackend.record.dto.SummonerDto;
import com.lolprojectbackend.record.dto.SummonerInfoDto;
import com.lolprojectbackend.record.entity.Summoner;
import com.lolprojectbackend.record.respository.SummonerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final RestTemplate restTemplate;
    private final SummonerRepository summonerRepository;

    @Value("${riot.api.key}")
    private String riotApiKey;

    /**
     * PUUID 조회 (Riot API 요청 + DB 저장 기능 추가)
     */
    public SummonerDto getPuuid(String gameName, String tagLine) {
        String url = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine;

        // HTTP 헤더 설정 (API Key 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // Riot API 키를 헤더에 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Riot API 요청 및 응답 처리
        ResponseEntity<SummonerDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, SummonerDto.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            SummonerDto summonerDto = response.getBody();

            //  DB에서 기존 소환사 조회 (PUUID 기준)
            Optional<Summoner> existingSummoner = summonerRepository.findByPuuid(summonerDto.getPuuid());
            if (existingSummoner.isPresent()) {
                log.info("이미 존재하는 소환사 (PUUID): {}", existingSummoner.get());
                return summonerDto; // 이미 존재하면 Riot API 응답만 반환
            }

            // 새로운 소환사 정보를 DB에 저장
            Summoner newSummoner = new Summoner();
            newSummoner.setPuuid(summonerDto.getPuuid());
            newSummoner.setName(gameName);
            newSummoner.setTag(tagLine);

            summonerRepository.save(newSummoner);
            log.info("새로운 소환사 저장 완료: {}", newSummoner);

            return summonerDto;
        } else {
            throw new RuntimeException("라이엇 API 요청 실패: " + response.getStatusCode());
        }
    }


    /**
     * 챔피언 숙련도 높은 3개 조회
     */
    public ChampionMasteryListDto getTopChampions(String puuid) {
        String url = "https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/top?count=3";

        // HTTP 헤더 설정 (API Key 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // API 키를 헤더에 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 요청 및 응답 처리
        ResponseEntity<ChampionMasteryDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ChampionMasteryDto[].class);

        // 응답이 null이면 빈 리스트 반환
        List<ChampionMasteryDto> masteryList = response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
        return new ChampionMasteryListDto(masteryList);
    }

    /**
     * Summoner ID로 랭크 정보 조회
     */
    public List<RankDto> getRankBySummonerId(String encryptedSummonerId) {
        String url = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId;

        // HTTP 헤더 설정 (API Key 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // API 키를 헤더에 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Riot API 요청 및 응답 처리
        ResponseEntity<RankDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, RankDto[].class);

        // 응답이 null이면 빈 리스트 반환
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }


    /**
     * 현재 인게임 정보 조회 (404 오류 처리 추가) (puuid 사용)
     * 게임 중이라면
     * {
     *     "gameId": 1234567890,
     *     "gameMode": "CLASSIC",
     *     "gameStartTime": 1700000000000,
     *     "participants": [
     *         {
     *             "summonerName": "정글못하면짐",
     *             "championId": 64,
     *             "teamId": 100
     *         }
     *     ]
     * } 리턴
     * 아니라면 null 리턴
     */
    public InGameInfoDto getInGameInfo(String encryptedSummonerId) {
        String url = "https://kr.api.riotgames.com/lol/spectator/v5/active-games/by-summoner/" + encryptedSummonerId;

        // HTTP 헤더 설정 (API Key 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // API 키를 헤더에 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Riot API 요청 및 응답 처리
            ResponseEntity<InGameInfoDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, InGameInfoDto.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("사용자가 현재 게임 중이 아닙니다. (404 Not Found)");
            return null; // 게임 중이 아닐 경우 null 반환
        } catch (HttpStatusCodeException e) {
            log.error("Riot API 호출 오류: {}", e.getMessage());
            throw new RuntimeException("Riot API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * Riot API를 사용하여 PUUID 기반 Summoner 정보 조회
     */
    public SummonerInfoDto getSummonerByPuuid(String puuid) {
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid;

        // HTTP 헤더 설정 (API Key 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Riot API 요청 및 응답 처리
            ResponseEntity<SummonerInfoDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, SummonerInfoDto.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Summoner 정보를 찾을 수 없습니다. (404 Not Found) - PUUID: {}", puuid);
            return null;
        } catch (HttpClientErrorException e) {
            log.error("Riot API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Riot API 호출 중 오류 발생: " + e.getMessage());
        }
    }

}
