package com.lanfunoe.gocache.service.data;

import com.lanfunoe.gocache.dto.EverydayRecommendResponse;
import com.lanfunoe.gocache.model.Album;
import com.lanfunoe.gocache.model.AlbumSong;
import com.lanfunoe.gocache.model.Artist;
import com.lanfunoe.gocache.model.ArtistSong;
import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongQuality;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 每日推荐数据转换器
 * 负责DTO ↔ Entity双向转换 + Entity → Response组装
 */
@Slf4j
@Component
public class DailyRecommendConverter {

    // ==================== DTO → Entity ====================

    /**
     * SongItem → DailyRecommend Entity
     */
    public DailyRecommend toDailyRecommend(String date, String userId, EverydayRecommendResponse.SongItem item) {
        if (item == null) {
            return null;
        }

        DailyRecommend dr = new DailyRecommend();
        dr.setUserId(userId);
        dr.setRecommendDate(date);
        dr.setSongHash(item.hash());
        dr.setAudioId(item.songid());
        dr.setOriAudioName(item.oriAudioName());

        return dr;
    }

    /**
     * SongItem → Song Entity
     */
    public Song toSong(EverydayRecommendResponse.SongItem item) {
        if (item == null) {
            return null;
        }

        Song song = new Song();
        song.setHash(item.hash());
        song.setAudioId(item.songid() != null ? item.songid() : 0L);
        song.setSongname(item.songname());
        song.setFilename(item.filename());
        song.setAuthorName(item.authorName());
        song.setSizableCover(item.sizableCover());
        song.setDuration(item.timeLength() != null ? item.timeLength().intValue() : null);
        song.setHash128(item.hash128());
        song.setHash320(item.hash320());
        song.setHashFlac(item.hashFlac());
        song.setMvhash(item.mvHash());
        song.setMixsongid(item.mixsongid());
        song.setPublishDate(item.publishDate());
        song.setLanguage(item.language());
        song.setAlbumId(item.albumId());
        song.setAlbumName(item.albumName());
        song.setBitrate(item.bitrate());
        song.setFilesize(item.fileSize());
        song.setExtname(item.extname());
        song.setRemark(item.remark());
        song.setPrivilege(item.privilege());
        return song;
    }

    /**
     * 批量转换: List<SongItem> → List<DailyRecommend>
     */
    public List<DailyRecommend> toDailyRecommendList(String date, String userId, List<EverydayRecommendResponse.SongItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(item -> toDailyRecommend(date, userId, item))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 批量转换: List<SongItem> → List<Song>
     */
    public List<Song> toSongList(List<EverydayRecommendResponse.SongItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toSong)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ==================== Entity → DTO ====================

    /**
     * Song → SongItem
     * 保留原有构建逻辑
     */
    public EverydayRecommendResponse.SongItem toSongItem(Song song, String oriAudioName) {
        if (song == null) {
            return null;
        }

        return new EverydayRecommendResponse.SongItem(
                null, // filesizeFlac
                null, // officialSongname
                oriAudioName, // oriAudioName
                null, // hash192
                song.getHashFlac(), // hashFlac
                song.getSongname(), // songname
                null, // musicTrac
                null, // isOriginal
                null, // payType
                null, // songType
                null, // albumId
                song.getRemark(), // remark
                song.getLanguage(), // language
                null, // isFileHead320
                null, // algPath
                null, // isFileHead
                song.getFilename(), // filename
                null, // cid
                null, // scid
                null, // suffixAudioName
                null, // recCopyWrite
                song.getMvhash(), // mvHash
                song.getHash(), // hash
                song.getAuthorName(), // authorName
                null, // tags
                null, // rankLabel
                null, // rankId
                song.getBitrate(), // bitrate
                null, // isMvFileHead
                null, // hasAccompany
                null, // filesize128
                song.getAlbumName(), // albumName
                null, // ztcMark
                null, // climaxEndTime
                song.getAudioId(), // songid
                null, // qualityLevel
                null, // filesize192
                song.getPublishDate(), // publishDate
                song.getFilesize(), // fileSize
                null, // hasAlbum
                song.getExtname(), // extname
                null, // type
                null, // filesize320
                null, // level
                song.getDuration(), // timeLength
                null, // recCopyWriteId
                null, // oldCpy
                null, // recLabelPrefix
                song.getHash128(), // hash128
                song.getHash320(), // hash320
                null, // relateGoods
                song.getMixsongid(), // mixsongid
                null, // hashOther
                song.getSizableCover(), // sizableCover
                null, // mvType
                null, // publishTime
                null, // filesizeApe
                null, // recLabelType
                null, // singerinfo
                null, // hashApe
                null, // transParam
                null, // timelength320
                null, // albumAudioRemark
                null, // albumAudioId
                null, // filesizeOther
                null, // ipsTags
                song.getPrivilege(), // privilege
                null, // failProcess
                null, // climaxStartTime
                null, // climaxTimelength
                null, // isPublish
                null // recSubCopyWrite
        );
    }

    // ==================== 新增：Album 转换 ====================

    /**
     * SongItem → Album Entity
     */
    public Album toAlbum(EverydayRecommendResponse.SongItem songItem) {
        if (songItem == null || songItem.albumId() == null) {
            return null;
        }
        return Album.builder()
                .albumId(songItem.albumId())
                .albumName(songItem.albumName())
                .sizableCover(songItem.sizableCover())
                .build();
    }

    /**
     * 批量转换: List<SongItem> → List<Album>（去重）
     */
    public List<Album> toAlbumList(List<EverydayRecommendResponse.SongItem> songItems) {
        if (songItems == null || songItems.isEmpty()) {
            return Collections.emptyList();
        }
        return songItems.stream()
                .filter(item -> item.albumId() != null)
                .map(this::toAlbum)
                .filter(Objects::nonNull)
                .distinct() // 去重（相同 album_id）
                .collect(Collectors.toList());
    }

    /**
     * SongItem → AlbumSong Entity
     */
    public AlbumSong toAlbumSong(EverydayRecommendResponse.SongItem songItem) {
        if (songItem == null || songItem.albumId() == null || songItem.songid() == null) {
            return null;
        }
        return AlbumSong.builder()
                .albumId(songItem.albumId())
                .audioId(songItem.songid())
                .songHash(songItem.hash())
                .trackNo(0) // SongItem 中没有曲目编号，默认为 0
                .build();
    }

    /**
     * 批量转换: List<SongItem> → List<AlbumSong>
     */
    public List<AlbumSong> toAlbumSongList(List<EverydayRecommendResponse.SongItem> songItems) {
        if (songItems == null || songItems.isEmpty()) {
            return Collections.emptyList();
        }
        return songItems.stream()
                .filter(item -> item.albumId() != null && item.songid() != null)
                .map(this::toAlbumSong)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ==================== 新增：Artist 转换 ====================

    /**
     * SingerInfo → Artist Entity
     */
    public Artist toArtist(EverydayRecommendResponse.SongItem.SingerInfo singerInfo) {
        if (singerInfo == null || singerInfo.id() == null) {
            return null;
        }
        Long artistId = parseLong(singerInfo.id());
        if (artistId == null) {
            return null;
        }
        return Artist.builder()
                .artistId(artistId)
                .authorName(singerInfo.name())
                .build();
    }

    /**
     * 批量转换: List<SongItem> → List<Artist>（从所有歌曲的歌手信息中提取，去重）
     */
    public List<Artist> toArtistList(List<EverydayRecommendResponse.SongItem> songItems) {
        if (songItems == null || songItems.isEmpty()) {
            return Collections.emptyList();
        }
        return songItems.stream()
                .filter(item -> item.singerinfo() != null && !item.singerinfo().isEmpty())
                .flatMap(item -> item.singerinfo().stream())
                .map(this::toArtist)
                .filter(Objects::nonNull)
                .distinct() // 去重
                .collect(Collectors.toList());
    }

    /**
     * SongItem → List<ArtistSong>（一首歌可能有多个歌手）
     */
    public List<ArtistSong> toArtistSongList(EverydayRecommendResponse.SongItem songItem) {
        if (songItem == null || songItem.singerinfo() == null || songItem.singerinfo().isEmpty()) {
            return Collections.emptyList();
        }

        List<ArtistSong> result = new ArrayList<>();
        Long audioId = songItem.songid();
        String songHash = songItem.hash();

        for (int i = 0; i < songItem.singerinfo().size(); i++) {
            EverydayRecommendResponse.SongItem.SingerInfo info = songItem.singerinfo().get(i);
            Long artistId = parseLong(info.id());
            if (artistId != null && audioId != null && songHash != null) {
                result.add(ArtistSong.builder()
                        .artistId(artistId)
                        .audioId(audioId)
                        .songHash(songHash)
                        .sort(i) // 使用索引作为排序
                        .build());
            }
        }
        return result;
    }

    /**
     * 批量转换: List<SongItem> → List<ArtistSong>（所有歌曲的所有歌手关联）
     */
    public List<ArtistSong> toAllArtistSongList(List<EverydayRecommendResponse.SongItem> songItems) {
        if (songItems == null || songItems.isEmpty()) {
            return Collections.emptyList();
        }
        return songItems.stream()
                .filter(item -> item.singerinfo() != null && !item.singerinfo().isEmpty())
                .flatMap(item -> toArtistSongList(item).stream())
                .collect(Collectors.toList());
    }

    // ==================== 新增：SongQuality 转换 ====================

    /**
     * SongItem → List<SongQuality>（一首歌的多个音质版本）
     */
    public List<SongQuality> toSongQualityList(EverydayRecommendResponse.SongItem songItem) {
        List<SongQuality> qualities = new ArrayList<>();
        Long audioId = songItem.songid();
        String songHash = songItem.hash();
        Integer privilege = songItem.privilege();

        if (audioId == null || songHash == null) {
            return qualities;
        }
        // 128K
        if (songItem.hash128() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(songItem.hash128())
                    .songHash(songHash)
                    .level(0)
                    .qualityVersion("128")
                    .filesize(songItem.filesize128())
                    .privilege(privilege)
                    .build());
        }


        // 192K
        if (songItem.hash192() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(songItem.hash192())
                    .songHash(songHash)
                    .level(1)
                    .qualityVersion("192")
                    .filesize(songItem.filesize192())
                    .privilege(privilege)
                    .build());
        }

        // 320K
        if (songItem.hash320() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(songItem.hash320())
                    .songHash(songHash)
                    .level(2)
                    .qualityVersion("320")
                    .filesize(songItem.filesize320())
                    .privilege(privilege)
                    .build());
        }

        // FLAC
        if (songItem.hashFlac() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(songItem.hashFlac())
                    .songHash(songHash)
                    .level(3)
                    .qualityVersion("FLAC")
                    .filesize(songItem.filesizeFlac())
                    .privilege(privilege)
                    .build());
        }

        // APE
        if (songItem.hashApe() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(songItem.hashApe())
                    .songHash(songHash)
                    .level(4)
                    .qualityVersion("APE")
                    .filesize(songItem.filesizeApe())
                    .privilege(privilege)
                    .build());

        }

        // OGG 128K (from trans_param)
        EverydayRecommendResponse.SongItem.TransParam transParam = songItem.transParam();
        if (transParam != null && transParam.ogg128Hash() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(transParam.ogg128Hash())
                    .songHash(songHash)
                    .level(0)
                    .qualityVersion("ogg128")
                    .filesize(transParam.ogg128Filesize())
                    .privilege(privilege)
                    .build());
        }

        // OGG 320K (from trans_param)
        if (transParam != null && transParam.ogg320Hash() != null) {
            qualities.add(SongQuality.builder()
                    .audioId(audioId)
                    .qualityHash(transParam.ogg320Hash())
                    .songHash(songHash)
                    .level(2)
                    .qualityVersion("ogg320")
                    .filesize(transParam.ogg320Filesize())
                    .privilege(privilege)
                    .build());
        }

        return qualities;
    }


    /**
     * 批量转换: List<SongItem> → List<SongQuality>（所有歌曲的所有音质版本）
     */
    public List<SongQuality> toAllSongQualityList(List<EverydayRecommendResponse.SongItem> songItems) {
        if (songItems == null || songItems.isEmpty()) {
            return Collections.emptyList();
        }
        return songItems.stream()
                .flatMap(item -> toSongQualityList(item).stream())
                .collect(Collectors.toList());
    }

    // ==================== 工具方法 ====================

    /**
     * 字符串解析为 Long（忽略解析错误）
     */
    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse Long from: {}", value);
            return null;
        }
    }

}