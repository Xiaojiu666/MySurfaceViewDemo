package com.sn.study_desgin_model.ImageLoader;

import android.graphics.Bitmap;

public interface ImageCache {

    void putImageCache(String key, Bitmap bmp);

    Bitmap getImageCache(String key);
}
