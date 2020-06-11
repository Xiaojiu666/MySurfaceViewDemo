package com.sn.study_desgin_model.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // default ImageCache
    private ImageCache mImageCache = new MemoryCache();

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public ImageLoader() {
    }

    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    public void displayImage(final String url, final ImageView imageView) {
        imageView.setTag(url);
        Bitmap mCacheBitmap = mImageCache.getImageCache(url);
        if (mCacheBitmap != null) {
            imageView.setImageBitmap(mCacheBitmap);
        } else {
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = downloadImage(url);
                    if (bitmap == null) {
                        return;
                    }
                    if (imageView.getTag().equals(url)) {
                        updataImageView(imageView, bitmap);
                    }
                    mImageCache.putImageCache(url, bitmap);
                }
            });
        }


    }

    private void updataImageView(final ImageView imageView, final Bitmap bmp) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bmp);
            }
        });
    }


    public Bitmap downloadImage(String imageUrl) {
        Bitmap mBitmap = null;
        try {
            URL mUrl = new URL(imageUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
            mBitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}
