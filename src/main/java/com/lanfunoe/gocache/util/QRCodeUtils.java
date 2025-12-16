package com.lanfunoe.gocache.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 *
 * 提供二维码生成功能，支持生成Base64格式的二维码图片
 *
 */
public class QRCodeUtils {

    /**
     * 生成二维码Base64字符串
     *
     * @param text 二维码内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @return Base64编码的二维码图片字符串
     */
    public static String generateQRCodeBase64(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "PNG", outputStream);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    /**
     * 生成二维码Base64字符串（默认尺寸200x200）
     *
     * @param text 二维码内容
     * @return Base64编码的二维码图片字符串
     */
    public static String generateQRCodeBase64(String text) {
        return generateQRCodeBase64(text, 200, 200);
    }
}