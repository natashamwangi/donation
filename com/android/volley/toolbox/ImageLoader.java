package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ImageLoader {
    private int mBatchResponseDelayMs;
    private final HashMap<String, BatchedImageRequest> mBatchedResponses;
    private final ImageCache mCache;
    private final Handler mHandler;
    private final HashMap<String, BatchedImageRequest> mInFlightRequests;
    private final RequestQueue mRequestQueue;
    private Runnable mRunnable;

    /* renamed from: com.android.volley.toolbox.ImageLoader.4 */
    class C00454 implements Runnable {
        C00454() {
        }

        public void run() {
            for (BatchedImageRequest bir : ImageLoader.this.mBatchedResponses.values()) {
                Iterator i$ = bir.mContainers.iterator();
                while (i$.hasNext()) {
                    ImageContainer container = (ImageContainer) i$.next();
                    if (container.mListener != null) {
                        if (bir.getError() == null) {
                            container.mBitmap = bir.mResponseBitmap;
                            container.mListener.onResponse(container, false);
                        } else {
                            container.mListener.onErrorResponse(bir.getError());
                        }
                    }
                }
            }
            ImageLoader.this.mBatchedResponses.clear();
            ImageLoader.this.mRunnable = null;
        }
    }

    private class BatchedImageRequest {
        private final LinkedList<ImageContainer> mContainers;
        private VolleyError mError;
        private final Request<?> mRequest;
        private Bitmap mResponseBitmap;

        public BatchedImageRequest(Request<?> request, ImageContainer container) {
            this.mContainers = new LinkedList();
            this.mRequest = request;
            this.mContainers.add(container);
        }

        public void setError(VolleyError error) {
            this.mError = error;
        }

        public VolleyError getError() {
            return this.mError;
        }

        public void addContainer(ImageContainer container) {
            this.mContainers.add(container);
        }

        public boolean removeContainerAndCancelIfNecessary(ImageContainer container) {
            this.mContainers.remove(container);
            if (this.mContainers.size() != 0) {
                return false;
            }
            this.mRequest.cancel();
            return true;
        }
    }

    public interface ImageCache {
        Bitmap getBitmap(String str);

        void putBitmap(String str, Bitmap bitmap);
    }

    public class ImageContainer {
        private Bitmap mBitmap;
        private final String mCacheKey;
        private final ImageListener mListener;
        private final String mRequestUrl;

        public ImageContainer(Bitmap bitmap, String requestUrl, String cacheKey, ImageListener listener) {
            this.mBitmap = bitmap;
            this.mRequestUrl = requestUrl;
            this.mCacheKey = cacheKey;
            this.mListener = listener;
        }

        public void cancelRequest() {
            if (this.mListener != null) {
                BatchedImageRequest request = (BatchedImageRequest) ImageLoader.this.mInFlightRequests.get(this.mCacheKey);
                if (request == null) {
                    request = (BatchedImageRequest) ImageLoader.this.mBatchedResponses.get(this.mCacheKey);
                    if (request != null) {
                        request.removeContainerAndCancelIfNecessary(this);
                        if (request.mContainers.size() == 0) {
                            ImageLoader.this.mBatchedResponses.remove(this.mCacheKey);
                        }
                    }
                } else if (request.removeContainerAndCancelIfNecessary(this)) {
                    ImageLoader.this.mInFlightRequests.remove(this.mCacheKey);
                }
            }
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public String getRequestUrl() {
            return this.mRequestUrl;
        }
    }

    /* renamed from: com.android.volley.toolbox.ImageLoader.2 */
    class C00922 implements Listener<Bitmap> {
        final /* synthetic */ String val$cacheKey;

        C00922(String str) {
            this.val$cacheKey = str;
        }

        public void onResponse(Bitmap response) {
            ImageLoader.this.onGetImageSuccess(this.val$cacheKey, response);
        }
    }

    /* renamed from: com.android.volley.toolbox.ImageLoader.3 */
    class C00933 implements ErrorListener {
        final /* synthetic */ String val$cacheKey;

        C00933(String str) {
            this.val$cacheKey = str;
        }

        public void onErrorResponse(VolleyError error) {
            ImageLoader.this.onGetImageError(this.val$cacheKey, error);
        }
    }

    public interface ImageListener extends ErrorListener {
        void onResponse(ImageContainer imageContainer, boolean z);
    }

    /* renamed from: com.android.volley.toolbox.ImageLoader.1 */
    static class C01011 implements ImageListener {
        final /* synthetic */ int val$defaultImageResId;
        final /* synthetic */ int val$errorImageResId;
        final /* synthetic */ ImageView val$view;

        C01011(int i, ImageView imageView, int i2) {
            this.val$errorImageResId = i;
            this.val$view = imageView;
            this.val$defaultImageResId = i2;
        }

        public void onErrorResponse(VolleyError error) {
            if (this.val$errorImageResId != 0) {
                this.val$view.setImageResource(this.val$errorImageResId);
            }
        }

        public void onResponse(ImageContainer response, boolean isImmediate) {
            if (response.getBitmap() != null) {
                this.val$view.setImageBitmap(response.getBitmap());
            } else if (this.val$defaultImageResId != 0) {
                this.val$view.setImageResource(this.val$defaultImageResId);
            }
        }
    }

    public ImageLoader(RequestQueue queue, ImageCache imageCache) {
        this.mBatchResponseDelayMs = 100;
        this.mInFlightRequests = new HashMap();
        this.mBatchedResponses = new HashMap();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mRequestQueue = queue;
        this.mCache = imageCache;
    }

    public static ImageListener getImageListener(ImageView view, int defaultImageResId, int errorImageResId) {
        return new C01011(errorImageResId, view, defaultImageResId);
    }

    public boolean isCached(String requestUrl, int maxWidth, int maxHeight) {
        throwIfNotOnMainThread();
        return this.mCache.getBitmap(getCacheKey(requestUrl, maxWidth, maxHeight)) != null;
    }

    public ImageContainer get(String requestUrl, ImageListener listener) {
        return get(requestUrl, listener, 0, 0);
    }

    public ImageContainer get(String requestUrl, ImageListener imageListener, int maxWidth, int maxHeight) {
        throwIfNotOnMainThread();
        String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
        Bitmap cachedBitmap = this.mCache.getBitmap(cacheKey);
        if (cachedBitmap != null) {
            ImageContainer container = new ImageContainer(cachedBitmap, requestUrl, null, null);
            imageListener.onResponse(container, true);
            return container;
        }
        ImageContainer imageContainer = new ImageContainer(null, requestUrl, cacheKey, imageListener);
        imageListener.onResponse(imageContainer, true);
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(imageContainer);
            return imageContainer;
        }
        Request<?> newRequest = new ImageRequest(requestUrl, new C00922(cacheKey), maxWidth, maxHeight, Config.RGB_565, new C00933(cacheKey));
        this.mRequestQueue.add(newRequest);
        this.mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest, imageContainer));
        return imageContainer;
    }

    public void setBatchedResponseDelay(int newBatchedResponseDelayMs) {
        this.mBatchResponseDelayMs = newBatchedResponseDelayMs;
    }

    private void onGetImageSuccess(String cacheKey, Bitmap response) {
        this.mCache.putBitmap(cacheKey, response);
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        if (request != null) {
            request.mResponseBitmap = response;
            batchResponse(cacheKey, request);
        }
    }

    private void onGetImageError(String cacheKey, VolleyError error) {
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        request.setError(error);
        if (request != null) {
            batchResponse(cacheKey, request);
        }
    }

    private void batchResponse(String cacheKey, BatchedImageRequest request) {
        this.mBatchedResponses.put(cacheKey, request);
        if (this.mRunnable == null) {
            this.mRunnable = new C00454();
            this.mHandler.postDelayed(this.mRunnable, (long) this.mBatchResponseDelayMs);
        }
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }
    }

    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight).append(url).toString();
    }
}
