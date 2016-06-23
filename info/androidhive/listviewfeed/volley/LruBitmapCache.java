package info.androidhive.listviewfeed.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {
    public static int getDefaultLruCacheSize() {
        return ((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8;
    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    protected int sizeOf(String key, Bitmap value) {
        return (value.getRowBytes() * value.getHeight()) / AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
    }

    public Bitmap getBitmap(String url) {
        return (Bitmap) get(url);
    }

    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
