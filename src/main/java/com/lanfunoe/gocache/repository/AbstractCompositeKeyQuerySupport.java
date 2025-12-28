package com.lanfunoe.gocache.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * 复合主键实体的批量查询支持基类
 * <p>
 * 提供通用的批量查询逻辑，子类只需提供字段映射即可。
 * 支持任意数量字段的复合主键（2-4个或更多）。
 *
 * @param <T>  实体类型
 * @param <ID> 复合主键ID类型
 */
@RequiredArgsConstructor
public abstract class AbstractCompositeKeyQuerySupport<T, ID extends Serializable> {

    protected final R2dbcEntityTemplate template;

    /**
     * 获取复合主键字段映射
     * <p>
     * 子类需要实现此方法，返回数据库字段名到ID对象getter方法的映射。
     * 例如：Map.of("hash", SongHashId::getHash, "audio_id", SongHashId::getAudioId)
     *
     * @return 字段名到getter方法的映射
     */
    protected abstract Map<String, Function<ID, Object>> getFieldMapping();

    /**
     * 获取实体类类型
     *
     * @return 实体类的Class对象
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 根据复合主键ID列表批量查询实体
     * <p>
     * 构建查询条件：(field1=? AND field2=?) OR (field1=? AND field2=?) OR ...
     * 自动处理空值、空集合和null ID对象。
     *
     * @param ids 复合主键ID的可迭代对象
     * @return 查询结果流
     */
    public final Flux<T> findAllByIds(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Flux.empty();
        }

        // 将 Iterable 转换为列表并过滤null值
        List<ID> idList = StreamSupport.stream(ids.spliterator(), false)
                .filter(Objects::nonNull)
                .toList();

        if (idList.isEmpty()) {
            return Flux.empty();
        }

        // 获取字段映射
        Map<String, Function<ID, Object>> fieldMapping = getFieldMapping();

        // 构建 OR 条件：每个ID对应一个AND条件组合
        List<Criteria> orConditions = idList.stream()
                .map(id -> buildAndCriteriaForId(id, fieldMapping))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (orConditions.isEmpty()) {
            return Flux.empty();
        }

        // 合并所有 OR 条件
        Criteria finalCriteria = orConditions.getFirst();
        for (int i = 1; i < orConditions.size(); i++) {
            finalCriteria = finalCriteria.or(orConditions.get(i));
        }

        return template.select(getEntityClass()).matching(Query.query(finalCriteria)).all();
    }

    /**
     * 为单个ID对象构建AND查询条件
     *
     * @param id          复合主键ID对象
     * @param fieldMapping 字段映射
     * @return 如果有非空字段则返回Criteria，否则返回空Optional
     */
    private Optional<Criteria> buildAndCriteriaForId(ID id, Map<String, Function<ID, Object>> fieldMapping) {
        Criteria andCriteria = Criteria.empty();
        boolean hasCondition = false;

        for (Map.Entry<String, Function<ID, Object>> entry : fieldMapping.entrySet()) {
            String columnName = entry.getKey();
            Object value = entry.getValue().apply(id);

            if (value != null) {
                andCriteria = andCriteria.and(columnName).is(value);
                hasCondition = true;
            }
        }

        return hasCondition ? Optional.of(andCriteria) : Optional.empty();
    }
}