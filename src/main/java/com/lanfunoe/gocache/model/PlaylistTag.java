package com.lanfunoe.gocache.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("playlist_tag")
@Data
public class PlaylistTag {

    @Column("global_collection_id")
    private String globalCollectionId;
    

    @Column("tag_id")
    private Long tagId;

    public PlaylistTagId getId() {
        return new PlaylistTagId(globalCollectionId, tagId);
    }
}