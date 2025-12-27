package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌曲音质版本复合主键
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongQualityId {
    /**
     * 音频ID
     */
    private Long audioId;

    /**
     * 音质Hash
     */
    private String qualityHash;
}
