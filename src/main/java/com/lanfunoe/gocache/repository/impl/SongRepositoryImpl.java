package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongHashId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.SongRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * 歌曲 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 * 复合主键: (hash, audio_id)
 */
@Slf4j
@Component
public class SongRepositoryImpl extends AbstractCompositeKeyQuerySupport<Song, SongHashId> implements SongRepositoryCustom {

    private final DatabaseClient databaseClient;

    public SongRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<SongHashId, Object>> getFieldMapping() {
        return Map.of(
                "hash", SongHashId::getHash,
                "audio_id", SongHashId::getAudioId
        );
    }

    @Override
    protected Class<Song> getEntityClass() {
        return Song.class;
    }

    @Override
    public Flux<Song> findAllIn(Iterable<Song> songs) {
        if (songs == null || !songs.iterator().hasNext()) {
            return Flux.empty();
        }

        // 定义字段映射：字段名 -> getter方法
        final Map<String, Function<Song, Object>> fieldGetters = Map.ofEntries(
            Map.entry("hash", Song::getHash),
            Map.entry("audio_id", Song::getAudioId),
            Map.entry("mixsongid", Song::getMixsongid),
            Map.entry("songname", Song::getSongname),
            Map.entry("audio_name", Song::getAudioName),
            Map.entry("filename", Song::getFilename),
            Map.entry("author_name", Song::getAuthorName),
            Map.entry("album_id", Song::getAlbumId),
            Map.entry("album_name", Song::getAlbumName),
            Map.entry("cover", Song::getCover),
            Map.entry("sizable_cover", Song::getSizableCover),
            Map.entry("duration", Song::getDuration),
            Map.entry("bitrate", Song::getBitrate),
            Map.entry("filesize", Song::getFilesize),
            Map.entry("extname", Song::getExtname),
            Map.entry("remark", Song::getRemark),
            Map.entry("privilege", Song::getPrivilege),
            Map.entry("mvhash", Song::getMvhash),
            Map.entry("hash_128", Song::getHash128),
            Map.entry("hash_320", Song::getHash320),
            Map.entry("hash_flac", Song::getHashFlac),
            Map.entry("language", Song::getLanguage),
            Map.entry("publish_date", Song::getPublishDate),
            Map.entry("extra_info", Song::getExtraInfo),
            Map.entry("created_at", Song::getCreatedAt),
            Map.entry("updated_at", Song::getUpdatedAt)
        );

        // 为每个Song构建Criteria，过滤掉没有条件的对象
        List<Criteria> orConditions = StreamSupport.stream(songs.spliterator(), false)
            .filter(Objects::nonNull)
            .map(song -> buildCriteriaForSong(song, fieldGetters))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        if (orConditions.isEmpty()) {
            return Flux.empty();
        }
        Criteria finalCriteria = orConditions.getFirst();
        for (int i = 1; i < orConditions.size(); i++) {
            finalCriteria = finalCriteria.or(orConditions.get(i));
        }

        return template.select(Song.class).matching(Query.query(finalCriteria)).all();
    }

    /**
     * 为单个Song对象构建查询条件
     *
     * @param song Song实体对象
     * @param fieldGetters 字段名到getter方法的映射
     * @return 如果有非空字段则返回Criteria，否则返回空Optional
     */
    private Optional<Criteria> buildCriteriaForSong(Song song, Map<String, Function<Song, Object>> fieldGetters) {
        Criteria andCriteria = Criteria.empty();
        boolean hasCondition = false;

        for (Map.Entry<String, java.util.function.Function<Song, Object>> entry : fieldGetters.entrySet()) {
            String columnName = entry.getKey();
            Object value = entry.getValue().apply(song);

            if (value != null) {
                andCriteria = andCriteria.and(columnName).is(value);
                hasCondition = true;
            }
        }

        return hasCondition ? Optional.of(andCriteria) : Optional.empty();
    }

    @Override
    public Mono<Long> upsert(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（2个主键 + 26个非主键）
        String[] hashes = songs.stream()
                .map(Song::getHash)
                .toArray(String[]::new);
        Long[] audioIds = songs.stream()
                .map(Song::getAudioId)
                .toArray(Long[]::new);
        String[] mixsongids = songs.stream()
                .map(Song::getMixsongid)
                .toArray(String[]::new);
        String[] songnames = songs.stream()
                .map(Song::getSongname)
                .toArray(String[]::new);
        String[] audioNames = songs.stream()
                .map(Song::getAudioName)
                .toArray(String[]::new);
        String[] filenames = songs.stream()
                .map(Song::getFilename)
                .toArray(String[]::new);
        String[] authorNames = songs.stream()
                .map(Song::getAuthorName)
                .toArray(String[]::new);
        Long[] albumIds = songs.stream()
                .map(Song::getAlbumId)
                .toArray(Long[]::new);
        String[] albumNames = songs.stream()
                .map(Song::getAlbumName)
                .toArray(String[]::new);
        String[] covers = songs.stream()
                .map(Song::getCover)
                .toArray(String[]::new);
        String[] sizableCovers = songs.stream()
                .map(Song::getSizableCover)
                .toArray(String[]::new);
        Integer[] durations = songs.stream()
                .map(Song::getDuration)
                .toArray(Integer[]::new);
        Integer[] bitrates = songs.stream()
                .map(Song::getBitrate)
                .toArray(Integer[]::new);
        Long[] fileSizes = songs.stream()
                .map(Song::getFilesize)
                .toArray(Long[]::new);
        String[] extnames = songs.stream()
                .map(Song::getExtname)
                .toArray(String[]::new);
        String[] remarks = songs.stream()
                .map(Song::getRemark)
                .toArray(String[]::new);
        Integer[] privileges = songs.stream()
                .map(Song::getPrivilege)
                .toArray(Integer[]::new);
        String[] mvhashes = songs.stream()
                .map(Song::getMvhash)
                .toArray(String[]::new);
        String[] hash128s = songs.stream()
                .map(Song::getHash128)
                .toArray(String[]::new);
        String[] hash320s = songs.stream()
                .map(Song::getHash320)
                .toArray(String[]::new);
        String[] hashFlacs = songs.stream()
                .map(Song::getHashFlac)
                .toArray(String[]::new);
        String[] languages = songs.stream()
                .map(Song::getLanguage)
                .toArray(String[]::new);
        String[] publishDates = songs.stream()
                .map(Song::getPublishDate)
                .toArray(String[]::new);
        String[] extraInfos = songs.stream()
                .map(Song::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = songs.stream()
                .map(Song::getCreatedAt)
                .toArray(LocalDateTime[]::new);
        LocalDateTime[] updatedATs = songs.stream()
                .map(Song::getUpdatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO song (hash, audio_id, mixsongid, songname, audio_name,
                             filename, author_name, album_id, album_name, cover,
                             sizable_cover, duration, bitrate, filesize, extname,
                             remark, privilege, mvhash, hash_128, hash_320, hash_flac,
                             language, publish_date, extra_info, created_at, updated_at)
            SELECT * FROM UNNEST($1::varchar[], $2::bigint[], $3::varchar[], $4::varchar[],
                                 $5::varchar[], $6::varchar[], $7::varchar[], $8::bigint[],
                                 $9::varchar[], $10::varchar[], $11::varchar[], $12::integer[],
                                 $13::integer[], $14::bigint[], $15::varchar[], $16::varchar[],
                                 $17::integer[], $18::varchar[], $19::varchar[], $20::varchar[],
                                 $21::varchar[], $22::varchar[], $23::varchar[], $24::varchar[],
                                 $25::timestamp[], $26::timestamp[])
            ON CONFLICT (hash, audio_id) DO UPDATE SET
                mixsongid = EXCLUDED.mixsongid,
                songname = EXCLUDED.songname,
                audio_name = EXCLUDED.audio_name,
                filename = EXCLUDED.filename,
                author_name = EXCLUDED.author_name,
                album_id = EXCLUDED.album_id,
                album_name = EXCLUDED.album_name,
                cover = EXCLUDED.cover,
                sizable_cover = EXCLUDED.sizable_cover,
                duration = EXCLUDED.duration,
                bitrate = EXCLUDED.bitrate,
                filesize = EXCLUDED.filesize,
                extname = EXCLUDED.extname,
                remark = EXCLUDED.remark,
                privilege = EXCLUDED.privilege,
                mvhash = EXCLUDED.mvhash,
                hash_128 = EXCLUDED.hash_128,
                hash_320 = EXCLUDED.hash_320,
                hash_flac = EXCLUDED.hash_flac,
                language = EXCLUDED.language,
                publish_date = EXCLUDED.publish_date,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at,
                updated_at = EXCLUDED.updated_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", hashes)
                .bind("$2", audioIds)
                .bind("$3", mixsongids)
                .bind("$4", songnames)
                .bind("$5", audioNames)
                .bind("$6", filenames)
                .bind("$7", authorNames)
                .bind("$8", albumIds)
                .bind("$9", albumNames)
                .bind("$10", covers)
                .bind("$11", sizableCovers)
                .bind("$12", durations)
                .bind("$13", bitrates)
                .bind("$14", fileSizes)
                .bind("$15", extnames)
                .bind("$16", remarks)
                .bind("$17", privileges)
                .bind("$18", mvhashes)
                .bind("$19", hash128s)
                .bind("$20", hash320s)
                .bind("$21", hashFlacs)
                .bind("$22", languages)
                .bind("$23", publishDates)
                .bind("$24", extraInfos)
                .bind("$25", createdATs)
                .bind("$26", updatedATs)
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