package com.lanfunoe.gocache.service.data;

import com.lanfunoe.gocache.dto.TagResponse;
import com.lanfunoe.gocache.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 标签数据转换器
 * 专门处理标签数据的层级结构转换
 */
@Slf4j
@Component
public class TagDataConverter {

    /**
     * 将扁平化的Tag列表转换为嵌套的TagResponse结构
     *
     * @param tags 扁平化的标签列表
     * @return TagResponse列表
     */
    public List<TagResponse> convertToTagsResponse(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        // 分组：顶级标签和子标签
        java.util.Map<Boolean, List<Tag>> partitioned = tags.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(tag ->
                    tag.getParentid() == null || tag.getParentid() == 0));

        List<Tag> topLevelTags = partitioned.get(true);
        List<Tag> childTags = partitioned.get(false);

        // 创建parentId到子标签列表的映射
        java.util.Map<Long, List<Tag>> childrenMap = childTags.stream()
                .filter(tag -> tag != null && tag.getParentid() != null)
                .collect(Collectors.groupingBy(Tag::getParentid));

        // 转换顶级标签
        return topLevelTags.stream()
                .filter(topTag -> topTag != null && topTag.getTagid() != null && topTag.getTagname() != null)
                .map(topTag -> {
                    List<TagResponse.SonTag> sonTags = childrenMap.getOrDefault(topTag.getTagid(), List.of())
                            .stream()
                            .filter(childTag -> childTag.getTagid() != null && childTag.getTagname() != null)
                            .map(childTag -> new TagResponse.SonTag(
                                    childTag.getParentid() != null ? String.valueOf(childTag.getParentid()) : "0",
                                    String.valueOf(childTag.getTagid()),
                                    childTag.getTagname(),
                                    childTag.getSort() != null ? String.valueOf(childTag.getSort()) : "0"
                            ))
                            .collect(Collectors.toList());

                    return new TagResponse(
                            topTag.getParentid() != null ? String.valueOf(topTag.getParentid()) : "0",
                            topTag.getSort() != null ? String.valueOf(topTag.getSort()) : "0",
                            String.valueOf(topTag.getTagid()),
                            topTag.getTagname(),
                            sonTags
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 将嵌套的TagResponse结构转换为扁平化的Tag实体列表
     *
     * @param tagResponses 嵌套的TagResponse列表
     * @return 扁平化的Tag实体列表
     */
    public List<Tag> convertToTags(List<TagResponse> tagResponses) {
        if (tagResponses == null || tagResponses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Tag> tags = new ArrayList<>();

        for (TagResponse parentTag : tagResponses) {
            // 转换父标签
            Tag parentEntity = new Tag();
            parentEntity.setTagid(Long.valueOf(parentTag.tagId()));
            parentEntity.setTagname(parentTag.tagName());
            parentEntity.setParentid(parentTag.parentId() != null && !parentTag.parentId().equals("0") ?
                Long.valueOf(parentTag.parentId()) : 0L);
            parentEntity.setSort(parentTag.sort() != null ? Integer.valueOf(parentTag.sort()) : 0);
            tags.add(parentEntity);

            // 转换子标签
            if (parentTag.son() != null) {
                for (TagResponse.SonTag sonTag : parentTag.son()) {
                    Tag sonEntity = new Tag();
                    sonEntity.setTagid(Long.valueOf(sonTag.tagId()));
                    sonEntity.setTagname(sonTag.tagName());
                    sonEntity.setParentid(sonTag.parentId() != null && !sonTag.parentId().equals("0") ?
                        Long.valueOf(sonTag.parentId()) : parentEntity.getTagid());
                    sonEntity.setSort(sonTag.sort() != null ? Integer.valueOf(sonTag.sort()) : 0);
                    tags.add(sonEntity);
                }
            }
        }

        return tags;
    }
}