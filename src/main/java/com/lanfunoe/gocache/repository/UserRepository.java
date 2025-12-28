package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.User;
import com.lanfunoe.gocache.repository.custom.UserRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 用户 Repository 接口
 */
public interface UserRepository extends R2dbcRepository<User, String>, UserRepositoryCustom {
}
