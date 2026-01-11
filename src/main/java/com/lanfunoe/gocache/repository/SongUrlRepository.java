package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.SongUrl;
import com.lanfunoe.gocache.repository.custom.SongUrlRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SongUrlRepository extends R2dbcRepository<SongUrl, Long>, SongUrlRepositoryCustom {
    Mono<SongUrl> findByHashAndQuality(String hash, String quality);
}