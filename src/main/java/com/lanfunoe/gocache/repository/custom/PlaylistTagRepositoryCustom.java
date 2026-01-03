package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.PlaylistTag;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PlaylistTagRepositoryCustom {
    Mono<Long> upsert(List<PlaylistTag> playlistTags);
}