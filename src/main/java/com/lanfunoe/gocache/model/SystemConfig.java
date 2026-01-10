package com.lanfunoe.gocache.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 系统配置实体
 */
@Table("system_config")
@Data
public class SystemConfig {

    @Id
    @Column("config_key")
    private String configKey;

    @Column("config_value")
    private String configValue;

    @Column("description")
    private String description;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}