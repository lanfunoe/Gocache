package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 歌曲URL复合主键
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongUrlId implements Serializable {
    private Long audioId;
    private String hash;
    private String quality;
}