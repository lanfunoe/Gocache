package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Playlist复合主键
 * 由 global_collection_id 和 category_id 组成
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistId implements Serializable {
    
    private String globalCollectionId;
    
    private Integer categoryId;

    public static PlaylistId of(Playlist playlist) {
        return new PlaylistId(playlist.getGlobalCollectionId(), playlist.getCategoryId());
    }
    
    public static PlaylistId of(String globalCollectionId, Integer categoryId) {
        return new PlaylistId(globalCollectionId, categoryId);
    }
}