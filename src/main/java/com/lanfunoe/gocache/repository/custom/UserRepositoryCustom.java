package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.User;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 用户 Repository 自定义方法接口
 */
public interface UserRepositoryCustom {
    Mono<Long> upsert(List<User> users);

    /**
     * Upsert 单个用户
     *
     * @param user 用户对象
     * @return 影响的行数
     */
    default Mono<Long> upsert(User user) {
        return upsert(java.util.Collections.singletonList(user));
    }
}
