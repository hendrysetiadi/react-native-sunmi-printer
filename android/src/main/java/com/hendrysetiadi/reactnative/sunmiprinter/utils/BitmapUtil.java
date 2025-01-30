package com.hendrysetiadi.reactnative.sunmiprinter.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtil {
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

