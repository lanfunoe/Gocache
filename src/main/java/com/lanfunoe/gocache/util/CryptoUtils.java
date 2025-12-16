package com.lanfunoe.gocache.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密工具类
 *
 * 提供AES、RSA等加密解密功能
 *
 */
@Slf4j
public class CryptoUtils {

    static {
            Security.addProvider(new BouncyCastleProvider());
    }

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String RSA_NO_PADDING_TRANSFORMATION = "RSA/ECB/NoPadding";
    private static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIAG7QOELSYoIJvTFJhMpe1s/gbjDJX51HBNnEl5HXqTW6lQ7LC8jr9fWZTwusknp+sVGzwd40MwP6U5yDE27M/X1+UR4tvOGOqp94TJtQ1EPnWGWXngpeIW5GxoQGao1rmYWAu6oi1z9XkChrsUdC6DJE5E221wf/4WLFxwAtRQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    private static final String LITE_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDECi0Np2UR87scwrvTr72L6oO01rBbbBPriSDFPxr3Z5syug0O24QyQO8bg27+0+4kBzTBTBOZ/WWU0WryL1JSXRTXLgFVxtzIY41Pe7lPOgsfTCn5kZcvKhYKJesKnnJDNr5/abvTGf+rHG3YRwsCHcQ08/q6ifSioBszvb3QiwIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    /**
     * AES加密
     *
     * @param data 待加密数据
     * @param key  加密密钥
     * @return 加密结果，包含加密字符串和密钥
     */
    public static AESResult aesEncrypt(String data, String key) {
        try {
            // 生成随机IV
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);

            // 创建密钥规范
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 加密数据
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // 组合IV和加密数据
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            // Base64编码
            String encryptedStr = Base64.getEncoder().encodeToString(combined);

            return new AESResult(encryptedStr, key);

        } catch (Exception e) {
            log.error("AES加密失败", e);
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密数据
     * @param key           解密密钥
     * @return 解密后的数据
     */
    public static String aesDecrypt(String encryptedData, String key) {
        try {
            // Base64解码
            byte[] combined = Base64.getDecoder().decode(encryptedData);

            // 提取IV和加密数据
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[combined.length - 16];
            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

            // 创建密钥和IV规范
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 解密数据
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("AES解密失败", e);
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 获取RSA公钥（根据 platform 选择：lite / normal）
     */
    private static PublicKey getPublicKey(boolean isLite) throws Exception {
        String publicKeyPEM = (isLite ? LITE_PUBLIC_KEY : PUBLIC_KEY)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密（根据 platform 选择公钥：lite / normal）
     *
     * @param data   待加密数据
     * @param isLite 是否为 lite 平台
     * @return 加密后的字符串（大写）
     */
    public static String rsaEncrypt(String data, boolean isLite) {
        try {
            PublicKey publicKey = getPublicKey(isLite);

            // 创建密码器
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 加密数据
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Base64编码并转为大写
            return Base64.getEncoder().encodeToString(encrypted).toUpperCase();

        } catch (Exception e) {
            log.error("RSA加密失败", e);
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        String chars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * 生成AES密钥
     *
     * @return 16位的AES密钥
     */
    public static String generateAESKey() {
        return generateRandomString(16);
    }

    /**
     * MD5加密
     *
     * @param data 待加密数据
     * @return MD5哈希值
     */
    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            log.error("MD5加密失败", e);
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * RSA加密2（用于云盘等特殊场景）
     *
     * @param data 待加密数据
     * @return 加密后的字符串
     */
    public static String rsaEncrypt2(String data) {
        return rsaEncrypt2(data, false);
    }

    /**
     * RSA加密2（用于云盘等特殊场景，根据 platform 选择公钥：lite / normal）
     *
     * @param data   待加密数据
     * @param isLite 是否为 lite 平台
     * @return 加密后的字符串
     */
    public static String rsaEncrypt2(String data, boolean isLite) {
        try {
            PublicKey publicKey = getPublicKey(isLite);

            // 创建密码器，使用PKCS1Padding
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 加密数据
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            log.error("RSA加密2失败", e);
            throw new RuntimeException("RSA加密2失败", e);
        }
    }

    /**
     * RSA加密（无填充，用于关注列表等场景，根据 platform 选择公钥：lite / normal）
     *
     * @param data   待加密数据
     * @param isLite 是否为 lite 平台
     * @return 加密后的十六进制字符串（大写）
     */
    public static String rsaEncryptNoPadding(String data, boolean isLite) {
        try {
            PublicKey publicKey = getPublicKey(isLite);

            // 创建密码器，使用NoPadding
            Cipher cipher = Cipher.getInstance(RSA_NO_PADDING_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 准备数据：转换为字节数组并填充到128字节
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            if (dataBytes.length > 128) {
                throw new IllegalArgumentException("Data too long for RSA encryption (max 128 bytes)");
            }

            byte[] paddedData = new byte[128];
            System.arraycopy(dataBytes, 0, paddedData, 0, dataBytes.length);
            // Remaining bytes are already 0 (default value)

            // 加密数据
            byte[] encrypted = cipher.doFinal(paddedData);

            // 转换为十六进制字符串并转为大写
            StringBuilder hexString = new StringBuilder();
            for (byte b : encrypted) {
                hexString.append(String.format("%02X", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            log.error("RSA无填充加密失败", e);
            throw new RuntimeException("RSA无填充加密失败", e);
        }
    }

    /**
     * 歌单AES加密
     *
     * @param data 待加密数据
     * @return 加密结果
     */
    public static AESResult playlistAesEncrypt(String data) {
        try {
            String key = generateRandomString(6).toLowerCase();
            String encryptKey = md5(key).substring(0, 16);
            String iv = md5(key).substring(16, 32);

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            String encryptedStr = Base64.getEncoder().encodeToString(encrypted);

            return new AESResult(encryptedStr, key);

        } catch (Exception e) {
            log.error("歌单AES加密失败", e);
            throw new RuntimeException("歌单AES加密失败", e);
        }
    }

    /**
     * 歌单AES解密
     *
     * @param encryptedData 加密数据
     * @param key 密钥
     * @return 解密后的数据
     */
    public static String playlistAesDecrypt(String encryptedData, String key) {
        try {
            String encryptKey = md5(key).substring(0, 16);
            String iv = md5(key).substring(16, 32);

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decodedData);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("歌单AES解密失败", e);
            throw new RuntimeException("歌单AES解密失败", e);
        }
    }

    /**
     * AES加密结果封装类
     */
    public static class AESResult {
        private final String str;
        private final String key;

        public AESResult(String str, String key) {
            this.str = str;
            this.key = key;
        }

        public String getStr() {
            return str;
        }

        public String getKey() {
            return key;
        }
    }
}
