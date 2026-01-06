package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.User;
import com.lanfunoe.gocache.repository.custom.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Long> upsert(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组
        String[] userIds = users.stream()
                .map(User::getUserId)
                .toArray(String[]::new);
        String[] nicknames = users.stream()
                .map(User::getNickname)
                .toArray(String[]::new);
        String[] bgPics = users.stream()
                .map(User::getBgPic)
                .toArray(String[]::new);
        String[] pics = users.stream()
                .map(User::getPic)
                .toArray(String[]::new);
        Integer[] genders = users.stream()
                .map(User::getGender)
                .toArray(Integer[]::new);
        Integer[] pGrades = users.stream()
                .map(User::getPGrade)
                .toArray(Integer[]::new);
        String[] descries = users.stream()
                .map(User::getDescri)
                .toArray(String[]::new);
        Integer[] follows = users.stream()
                .map(User::getFollows)
                .toArray(Integer[]::new);
        Integer[] fans = users.stream()
                .map(User::getFans)
                .toArray(Integer[]::new);
        Integer[] friends = users.stream()
                .map(User::getFriends)
                .toArray(Integer[]::new);
        Integer[] hvisitors = users.stream()
                .map(User::getHvisitors)
                .toArray(Integer[]::new);
        Integer[] durations = users.stream()
                .map(User::getDuration)
                .toArray(Integer[]::new);
        Long[] rtimes = users.stream()
                .map(User::getRtime)
                .toArray(Long[]::new);
        String[] extraInfos = users.stream()
                .map(User::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = users.stream()
                .map(User::getCreatedAt)
                .toArray(LocalDateTime[]::new);
        LocalDateTime[] updatedATs = users.stream()
                .map(User::getUpdatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO user (user_id, nickname, bg_pic, pic, gender, p_grade,
                             descri, follows, fans, friends, hvisitors, duration,
                             rtime, extra_info, created_at, updated_at)
            SELECT * FROM UNNEST($1::varchar[], $2::varchar[], $3::varchar[],
                                 $4::varchar[], $5::integer[], $6::integer[],
                                 $7::varchar[], $8::integer[], $9::integer[],
                                 $10::integer[], $11::integer[], $12::integer[],
                                 $13::bigint[], $14::varchar[], $15::timestamp[],
                                 $16::timestamp[])
            ON CONFLICT (user_id) DO UPDATE SET
                nickname = EXCLUDED.nickname,
                bg_pic = EXCLUDED.bg_pic,
                pic = EXCLUDED.pic,
                gender = EXCLUDED.gender,
                p_grade = EXCLUDED.p_grade,
                descri = EXCLUDED.descri,
                follows = EXCLUDED.follows,
                fans = EXCLUDED.fans,
                friends = EXCLUDED.friends,
                hvisitors = EXCLUDED.hvisitors,
                duration = EXCLUDED.duration,
                rtime = EXCLUDED.rtime,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at,
                updated_at = EXCLUDED.updated_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", userIds)
                .bind("$2", nicknames)
                .bind("$3", bgPics)
                .bind("$4", pics)
                .bind("$5", genders)
                .bind("$6", pGrades)
                .bind("$7", descries)
                .bind("$8", follows)
                .bind("$9", fans)
                .bind("$10", friends)
                .bind("$11", hvisitors)
                .bind("$12", durations)
                .bind("$13", rtimes)
                .bind("$14", extraInfos)
                .bind("$15", createdATs)
                .bind("$16", updatedATs)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(rowsUpdated -> log.info("Upserted {} rows", rowsUpdated))
                .onErrorResume(e -> {
                    log.error("Failed to upsert:", e);
                    return Mono.just(0L);
                })
                .contextCapture();
    }
}
