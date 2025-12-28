package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 歌曲音质版本复合主键
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongQualityId implements Serializable {
    /**
     * 音频ID
     */
    private Long audioId;

    /**
     * 音质Hash
     */
    private String qualityHash;
}
