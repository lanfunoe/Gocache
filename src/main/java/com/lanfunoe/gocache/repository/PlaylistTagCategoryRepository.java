package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.PlaylistTagCategory;
import reactor.core.publisher.Flux;

/**
 * 歌单标签分类 Repository 接口
 */
public interface PlaylistTagCategoryRepository extends ReactiveRepository<PlaylistTagCategory, Long> {

    /**
     * 根据父级标签ID查找子标签
     *
     * @param parentId 父级标签ID
     * @return 标签列表
     */
    Flux<PlaylistTagCategory> findByParentId(Long parentId);

    /**
     * 查找顶级标签（parentId = 0）
     *
     * @return 标签列表
     */
    Flux<PlaylistTagCategory> findTopLevelTags();

    /**
     * 查找仍在有效期内的标签（7天内更新过）
     *
     * @return 标签列表
     */
    Flux<PlaylistTagCategory> findValidTags();
}
