package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Tag;
import reactor.core.publisher.Flux;

/**
 * 标签 Repository 接口
 */
public interface TagRepository extends ReactiveRepository<Tag, Long> {

    /**
     * 根据父级标签ID查找子标签
     *
     * @param parentId 父级标签ID
     * @return 标签列表
     */
    Flux<Tag> findByParentId(Long parentId);

    /**
     * 查找顶级标签（parentId = 0）
     *
     * @return 标签列表
     */
    Flux<Tag> findTopLevelTags();

    /**
     * 查找仍在有效期内的标签（7天内更新过）
     *
     * @return 标签列表
     */
    Flux<Tag> findValidTags();
}
