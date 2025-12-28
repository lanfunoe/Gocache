package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 歌曲复合主键
 * 由 audio_id 和 hash 组成
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongHashId implements Serializable {

    /**
     * 音频ID
     */
    private Long audioId;

    /**
     * 歌曲Hash
     */
    private String hash;

    /**
     * 从 Song 实体创建主键
     */
    public static SongHashId of(Song song) {
        return new SongHashId(song.getAudioId(), song.getHash());
    }

    /**
     * 创建主键
     */
    public static SongHashId of(Long audioId, String hash) {
        return new SongHashId(audioId, hash);
    }
}