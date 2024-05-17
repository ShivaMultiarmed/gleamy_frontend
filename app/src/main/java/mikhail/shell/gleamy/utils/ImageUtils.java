package mikhail.shell.gleamy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class ImageUtils {
    public static Bitmap getCircleBitmap(byte[] imageBytes) {

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return circleBitmap;
    }
}
