package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Image;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 图片资源 Repository 接口
 */
public interface ImageRepository extends R2dbcRepository<Image, Long> {

}
