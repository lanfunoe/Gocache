package com.lanfunoe.gocache.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 * 提供常用的日期时间计算和格式化方法
 */
@Slf4j
public final class DateTimeUtils {

    private DateTimeUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 计算到当天24:00的持续时间
     *
     * @return 距离当天24:00的Duration
     */
    public static Duration calculateTillMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, midnight);
    }

    /**
     * 计算到指定日期24:00的持续时间
     *
     * @param date 指定日期
     * @return 距离指定日期24:00的Duration
     */
    public static Duration calculateTillMidnight(LocalDateTime date) {
        LocalDateTime midnight = date.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(date, midnight);
    }

    /**
     * 格式化日期为 yyyyMMdd
     *
     * @param dateTime 日期时间
     * @return 格式化后的日期字符串
     */
    public static String formatDateYMD(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 格式化当前日期为 yyyyMMdd
     *
     * @return 格式化后的当前日期字符串
     */
    public static String formatCurrentDateYMD() {
        return formatDateYMD(LocalDateTime.now());
    }

    /**
     * 格式化时长为可读字符串
     *
     * @param duration 时长
     * @return 格式化后的时长字符串（如：2小时30分钟15秒）
     */
    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0 || sb.length() == 0) {
            sb.append(seconds).append("秒");
        }

        return sb.toString();
    }

    /**
     * 判断两个时间是否在同一天
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 是否在同一天
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    /**
     * 判断指定时间是否是今天
     *
     * @param dateTime 要判断的时间
     * @return 是否是今天
     */
    public static boolean isToday(LocalDateTime dateTime) {
        return isSameDay(dateTime, LocalDateTime.now());
    }

    /**
     * 获取今天开始的时间（00:00:00）
     *
     * @return 今天开始的时间
     */
    public static LocalDateTime getStartOfToday() {
        return LocalDateTime.now().toLocalDate().atStartOfDay();
    }

    /**
     * 获取今天结束的时间（23:59:59.999999999）
     *
     * @return 今天结束的时间
     */
    public static LocalDateTime getEndOfToday() {
        return LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
    }
}