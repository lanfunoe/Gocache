package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.PlaylistTag;
import com.lanfunoe.gocache.model.PlaylistTagId;
import com.lanfunoe.gocache.repository.custom.PlaylistTagRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PlaylistTagRepository extends R2dbcRepository<PlaylistTag, PlaylistTagId>, PlaylistTagRepositoryCustom {
}