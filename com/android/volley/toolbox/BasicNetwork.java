package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG;
    private static int DEFAULT_POOL_SIZE;
    private static int SLOW_REQUEST_THRESHOLD_MS;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    static {
        DEBUG = VolleyLog.DEBUG;
        SLOW_REQUEST_THRESHOLD_MS = 3000;
        DEFAULT_POOL_SIZE = AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
    }

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        this.mHttpStack = httpStack;
        this.mPool = pool;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.volley.NetworkResponse performRequest(com.android.volley.Request<?> r18) throws com.android.volley.VolleyError {
        /*
        r17 = this;
        r11 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r9 = 0;
        r5 = 0;
        r13 = new java.util.HashMap;
        r13.<init>();
        r8 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r8.<init>();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r1 = r18.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r0 = r17;
        r0.addCacheHeaders(r8, r1);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r0 = r17;
        r1 = r0.mHttpStack;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r0 = r18;
        r9 = r1.performRequest(r0, r8);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r6 = r9.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r14 = r6.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r1 = r9.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r13 = convertHeaders(r1);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r1 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r14 != r1) goto L_0x0051;
    L_0x0037:
        r1 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r15 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r4 = r18.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        if (r4 != 0) goto L_0x004a;
    L_0x0041:
        r4 = 0;
    L_0x0042:
        r16 = 1;
        r0 = r16;
        r1.<init>(r15, r4, r13, r0);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
    L_0x0049:
        return r1;
    L_0x004a:
        r4 = r18.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r4 = r4.data;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        goto L_0x0042;
    L_0x0051:
        r1 = r9.getEntity();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        if (r1 == 0) goto L_0x008b;
    L_0x0057:
        r1 = r9.getEntity();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r0 = r17;
        r5 = r0.entityToBytes(r1);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
    L_0x0061:
        r15 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r2 = r15 - r11;
        r1 = r17;
        r4 = r18;
        r1.logSlowRequests(r2, r4, r5, r6);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r14 < r1) goto L_0x0076;
    L_0x0072:
        r1 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r14 <= r1) goto L_0x008f;
    L_0x0076:
        r1 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r1.<init>();	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        throw r1;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
    L_0x007c:
        r7 = move-exception;
        r1 = "socket";
        r4 = new com.android.volley.TimeoutError;
        r4.<init>();
        r0 = r18;
        attemptRetryOnException(r1, r0, r4);
        goto L_0x0004;
    L_0x008b:
        r1 = 0;
        r5 = new byte[r1];	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        goto L_0x0061;
    L_0x008f:
        r1 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        r4 = 0;
        r1.<init>(r14, r5, r13, r4);	 Catch:{ SocketTimeoutException -> 0x007c, ConnectTimeoutException -> 0x0096, MalformedURLException -> 0x00a5, IOException -> 0x00c3 }
        goto L_0x0049;
    L_0x0096:
        r7 = move-exception;
        r1 = "connection";
        r4 = new com.android.volley.TimeoutError;
        r4.<init>();
        r0 = r18;
        attemptRetryOnException(r1, r0, r4);
        goto L_0x0004;
    L_0x00a5:
        r7 = move-exception;
        r1 = new java.lang.RuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r15 = "Bad URL ";
        r4 = r4.append(r15);
        r15 = r18.getUrl();
        r4 = r4.append(r15);
        r4 = r4.toString();
        r1.<init>(r4, r7);
        throw r1;
    L_0x00c3:
        r7 = move-exception;
        r14 = 0;
        r10 = 0;
        if (r9 == 0) goto L_0x0104;
    L_0x00c8:
        r1 = r9.getStatusLine();
        r14 = r1.getStatusCode();
        r1 = "Unexpected response code %d for %s";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r15 = 0;
        r16 = java.lang.Integer.valueOf(r14);
        r4[r15] = r16;
        r15 = 1;
        r16 = r18.getUrl();
        r4[r15] = r16;
        com.android.volley.VolleyLog.m1e(r1, r4);
        if (r5 == 0) goto L_0x0110;
    L_0x00e8:
        r10 = new com.android.volley.NetworkResponse;
        r1 = 0;
        r10.<init>(r14, r5, r13, r1);
        r1 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r14 == r1) goto L_0x00f6;
    L_0x00f2:
        r1 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r14 != r1) goto L_0x010a;
    L_0x00f6:
        r1 = "auth";
        r4 = new com.android.volley.AuthFailureError;
        r4.<init>(r10);
        r0 = r18;
        attemptRetryOnException(r1, r0, r4);
        goto L_0x0004;
    L_0x0104:
        r1 = new com.android.volley.NoConnectionError;
        r1.<init>(r7);
        throw r1;
    L_0x010a:
        r1 = new com.android.volley.ServerError;
        r1.<init>(r10);
        throw r1;
    L_0x0110:
        r1 = new com.android.volley.NetworkError;
        r1.<init>(r10);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.BasicNetwork.performRequest(com.android.volley.Request):com.android.volley.NetworkResponse");
    }

    private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, StatusLine statusLine) {
        if (DEBUG || requestLifetime > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(requestLifetime);
            objArr[2] = responseContents != null ? Integer.valueOf(responseContents.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.m0d(str, objArr);
        }
    }

    private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
            throw e;
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
            }
        }
    }

    protected void logError(String what, String url, long start) {
        long now = SystemClock.elapsedRealtime();
        VolleyLog.m3v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(now - start), url);
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = this.mPool.getBuf(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                bytes.write(buffer, 0, count);
            }
            byte[] toByteArray = bytes.toByteArray();
            return toByteArray;
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                VolleyLog.m3v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(buffer);
            bytes.close();
        }
    }

    private static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new HashMap();
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }
}
