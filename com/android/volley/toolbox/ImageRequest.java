package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;

public class ImageRequest extends Request<Bitmap> {
    private static final float IMAGE_BACKOFF_MULT = 2.0f;
    private static final int IMAGE_MAX_RETRIES = 2;
    private static final int IMAGE_TIMEOUT_MS = 1000;
    private static final Object sDecodeLock;
    private final Config mDecodeConfig;
    private final Listener<Bitmap> mListener;
    private final int mMaxHeight;
    private final int mMaxWidth;

    static {
        sDecodeLock = new Object();
    }

    public ImageRequest(String url, Listener<Bitmap> listener, int maxWidth, int maxHeight, Config decodeConfig, ErrorListener errorListener) {
        super(0, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        this.mListener = listener;
        this.mDecodeConfig = decodeConfig;
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
    }

    public Priority getPriority() {
        return Priority.LOW;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            return (int) (((double) actualPrimary) * (((double) maxSecondary) / ((double) actualSecondary)));
        } else if (maxSecondary == 0) {
            return maxPrimary;
        } else {
            double ratio = ((double) actualSecondary) / ((double) actualPrimary);
            int resized = maxPrimary;
            if (((double) resized) * ratio > ((double) maxSecondary)) {
                resized = (int) (((double) maxSecondary) / ratio);
            }
            return resized;
        }
    }

    protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        Response<Bitmap> doParse;
        synchronized (sDecodeLock) {
            try {
                doParse = doParse(response);
            } catch (Throwable e) {
                Object[] objArr = new Object[IMAGE_MAX_RETRIES];
                objArr[0] = Integer.valueOf(response.data.length);
                objArr[1] = getUrl();
                VolleyLog.m1e("Caught OOM for %d byte image, url=%s", objArr);
                doParse = Response.error(new ParseError(e));
            }
        }
        return doParse;
    }

    private Response<Bitmap> doParse(NetworkResponse response) {
        Bitmap bitmap;
        byte[] data = response.data;
        Options decodeOptions = new Options();
        if (this.mMaxWidth == 0 && this.mMaxHeight == 0) {
            decodeOptions.inPreferredConfig = this.mDecodeConfig;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            int desiredWidth = getResizedDimension(this.mMaxWidth, this.mMaxHeight, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(this.mMaxHeight, this.mMaxWidth, actualHeight, actualWidth);
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            if (tempBitmap == null || (tempBitmap.getWidth() <= desiredWidth && tempBitmap.getHeight() <= desiredHeight)) {
                bitmap = tempBitmap;
            } else {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            }
        }
        if (bitmap == null) {
            return Response.error(new ParseError(response));
        }
        return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(response));
    }

    protected void deliverResponse(Bitmap response) {
        this.mListener.onResponse(response);
    }

    static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double ratio = Math.min(((double) actualWidth) / ((double) desiredWidth), ((double) actualHeight) / ((double) desiredHeight));
        float n = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        while (((double) (n * IMAGE_BACKOFF_MULT)) <= ratio) {
            n *= IMAGE_BACKOFF_MULT;
        }
        return (int) n;
    }
}
