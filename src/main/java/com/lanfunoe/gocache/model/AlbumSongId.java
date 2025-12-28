package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专辑-歌曲关联复合主键
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumSongId implements Serializable {
    /**
     * 专辑ID
     */
    private Long albumId;

    /**
     * 音频ID
     */
    private Long audioId;
}