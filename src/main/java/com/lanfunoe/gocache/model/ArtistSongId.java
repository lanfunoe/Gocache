package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 歌手-歌曲关联复合主键
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistSongId implements Serializable {
    /**
     * 歌手ID
     */
    private Long artistId;

    /**
     * 音频ID
     */
    private Long audioId;
}