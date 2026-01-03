package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistTagId implements Serializable {

    private String globalCollectionId;
    

    private Long tagId;

    public static PlaylistTagId of(String globalCollectionId, Long tagId) {
        return new PlaylistTagId(globalCollectionId, tagId);
    }
}