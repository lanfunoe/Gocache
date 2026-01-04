package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.SongUrl;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongUrlRepository extends R2dbcRepository<SongUrl, Long> {
}