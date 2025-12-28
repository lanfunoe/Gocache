package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.PlaylistTagCategory;
import com.lanfunoe.gocache.repository.custom.PlaylistTagCategoryRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌单标签分类 Repository 接口
 */
public interface PlaylistTagCategoryRepository extends R2dbcRepository<PlaylistTagCategory, Long>, PlaylistTagCategoryRepositoryCustom {
}
