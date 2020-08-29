package com.hendrysetiadi.reactnative.sunmiprinter.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Xml;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class BitmapUtil {

    /**
     * 生成条码bitmap
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int format, int width, int height) {
        if(content == null || content.equals(""))
            return null;
        BarcodeFormat barcodeFormat;
        switch (format){
            case 0:
                barcodeFormat = BarcodeFormat.UPC_A;
                break;
            case 1:
                barcodeFormat = BarcodeFormat.UPC_E;
                break;
            case 2:
                barcodeFormat = BarcodeFormat.EAN_13;
                break;
            case 3:
                barcodeFormat = BarcodeFormat.EAN_8;
                break;
            case 4:
                barcodeFormat = BarcodeFormat.CODE_39;
                break;
            case 5:
                barcodeFormat = BarcodeFormat.ITF;
                break;
            case 6:
                barcodeFormat = BarcodeFormat.CODABAR;
                break;
            case 7:
                barcodeFormat = BarcodeFormat.CODE_93;
                break;
            case 8:
                barcodeFormat = BarcodeFormat.CODE_128;
                break;
            case 9:
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            default:
                barcodeFormat = BarcodeFormat.QR_CODE;
                height = width;
                break;
        }
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, barcodeFormat, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m) {
        // Re-implement Bitmap createBitmap() to always return a mutable bitmap.
        Canvas canvas = new Canvas();

        Bitmap bitmap;
        Paint paint;
        if ((m == null) || m.isIdentity()) {
            bitmap = Bitmap.createBitmap(width, height, source.getConfig());
            paint = null;
        } else {
            RectF rect = new RectF(0, 0, width, height);
            m.mapRect(rect);
            bitmap = Bitmap.createBitmap(Math.round(rect.width()), Math.round(rect.height()), source.getConfig());

            canvas.translate(-rect.left, -rect.top);
            canvas.concat(m);

            paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            if (!m.rectStaysRect()) {
                paint.setAntiAlias(true);
            }
        }
        bitmap.setDensity(source.getDensity());
        canvas.setBitmap(bitmap);

        Rect srcBounds = new Rect(x, y, x + width, y + height);
        RectF dstBounds = new RectF(0, 0, width, height);
        canvas.drawBitmap(source, srcBounds, dstBounds, paint);
        return bitmap;
    }

    public static Rect getBitmapBounds(byte[] data){
        Rect bounds = new Rect();
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            bounds.right = options.outWidth;
            bounds.bottom = options.outHeight;
        } catch (Exception e) {
        }finally {
        }
        return bounds;
    }

    public static Bitmap decodeBitmap(byte[] data, int width, int height){
        Bitmap bitmap = null;
        try {
            Rect bounds = getBitmapBounds(data);
            int sampleSize = Math.max(bounds.width() / width, bounds.height() / height);
            sampleSize = Math.min(sampleSize,
                    Math.max(bounds.width() / height, bounds.height() / width));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(sampleSize, 1);
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);//!!!!溢出
        } catch (Exception e) {
        } finally {
            data = null;
        }

        // Ensure bitmap in 8888 format, good for editing as well as GL compatible.
        if ((bitmap != null) && (bitmap.getConfig() != Bitmap.Config.ARGB_8888)) {
            Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            bitmap.recycle();
            bitmap = copy;
        }

        if (bitmap != null) {
            // Scale down the sampled bitmap if it's still larger than the desired dimension.
            float scale = Math.min((float) width / bitmap.getWidth(),
                    (float) height / bitmap.getHeight());
            scale = Math.max(scale, Math.min((float) height / bitmap.getWidth(),
                    (float) width / bitmap.getHeight()));
            if (scale < 1) {
                Matrix m = new Matrix();
                m.setScale(scale, scale);
                Bitmap transformed = createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m);
                bitmap.recycle();
                return transformed;
            }
        }
        return bitmap;
    }
}
