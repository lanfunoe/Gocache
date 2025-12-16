package com.lanfunoe.gocache.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

/**
 * 歌词工具类
 *
 * 提供KRC格式歌词解码功能
 *
 */
@Slf4j
public class LyricsUtils {

    /**
     * 解码KRC格式歌词
     *
     * @param encodedData 编码的歌词数据（Base64字符串）
     * @return 解码后的歌词文本
     */
    public static String decodeKrcLyrics(String encodedData) {
        try {
            byte[] bytes = Base64.getDecoder().decode(encodedData);
            return decodeKrcLyrics(bytes);
        } catch (Exception e) {
            log.error("KRC歌词解码失败", e);
            return "";
        }
    }

    /**
     * 解码KRC格式歌词
     *
     * @param data 编码的歌词数据（字节数组）
     * @return 解码后的歌词文本
     */
    public static String decodeKrcLyrics(byte[] data) {
        try {
            if (data.length <= 4) {
                return "";
            }

            // KRC解密密钥
            byte[] enKey = {64, 71, 97, 119, 94, 50, 116, 71, 81, 54, 49, 45, -50, -46, 110, 105};

            // 跳过前4个字节
            byte[] krcBytes = new byte[data.length - 4];
            System.arraycopy(data, 4, krcBytes, 0, krcBytes.length);

            // XOR解密
            for (int i = 0; i < krcBytes.length; i++) {
                krcBytes[i] = (byte) (krcBytes[i] ^ enKey[i % enKey.length]);
            }

            // zlib解压缩
            return inflateZlib(krcBytes);

        } catch (Exception e) {
            log.error("KRC歌词解码失败", e);
            return "";
        }
    }

    /**
     * zlib解压缩
     *
     * @param compressedData 压缩的数据
     * @return 解压缩后的字符串
     */
    private static String inflateZlib(byte[] compressedData) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             InflaterInputStream iis = new InflaterInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = iis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }

            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("zlib解压缩失败，尝试使用原始数据", e);
            // 如果解压缩失败，尝试直接返回UTF-8字符串（兼容旧逻辑）
            return new String(compressedData, StandardCharsets.UTF_8);
        }
    }
}