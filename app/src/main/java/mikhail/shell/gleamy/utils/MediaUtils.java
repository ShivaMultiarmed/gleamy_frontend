package mikhail.shell.gleamy.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

public final class MediaUtils {
    public static byte[] getFileContent(@NonNull ContentResolver contentResolver, Uri uri) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        byte[] content = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            content = inputStream.readAllBytes();
        }
        inputStream.close();
        return content;
    }
    public static String getExtension(@NonNull ContentResolver contentResolver, Uri uri)
    {
        String mimeType = contentResolver.getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }
}
