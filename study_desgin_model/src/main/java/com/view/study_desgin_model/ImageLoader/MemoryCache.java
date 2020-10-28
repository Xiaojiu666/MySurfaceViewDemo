package com.view.study_desgin_model.ImageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initImageCache();
    }

    private void initImageCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheMemory = maxMemory / 4;
        mImageCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getWidth() / 1024;
            }
        };
    }

    public void putImageCache(String key, Bitmap value) {
        if (mImageCache != null) {
            mImageCache.put(key, value);
        }
    }

    @Override
    public Bitmap getImageCache(String key) {
        if (mImageCache != null) {
            return mImageCache.get(key);
        }
        return null;
    }
}
