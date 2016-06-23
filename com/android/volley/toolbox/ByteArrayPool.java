package com.android.volley.toolbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ByteArrayPool {
    protected static final Comparator<byte[]> BUF_COMPARATOR;
    private List<byte[]> mBuffersByLastUse;
    private List<byte[]> mBuffersBySize;
    private int mCurrentSize;
    private final int mSizeLimit;

    /* renamed from: com.android.volley.toolbox.ByteArrayPool.1 */
    static class C00431 implements Comparator<byte[]> {
        C00431() {
        }

        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    }

    static {
        BUF_COMPARATOR = new C00431();
    }

    public ByteArrayPool(int sizeLimit) {
        this.mBuffersByLastUse = new LinkedList();
        this.mBuffersBySize = new ArrayList(64);
        this.mCurrentSize = 0;
        this.mSizeLimit = sizeLimit;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized byte[] getBuf(int r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r1 = 0;
    L_0x0002:
        r2 = r4.mBuffersBySize;	 Catch:{ all -> 0x002d }
        r2 = r2.size();	 Catch:{ all -> 0x002d }
        if (r1 >= r2) goto L_0x002a;
    L_0x000a:
        r2 = r4.mBuffersBySize;	 Catch:{ all -> 0x002d }
        r0 = r2.get(r1);	 Catch:{ all -> 0x002d }
        r0 = (byte[]) r0;	 Catch:{ all -> 0x002d }
        r2 = r0.length;	 Catch:{ all -> 0x002d }
        if (r2 < r5) goto L_0x0027;
    L_0x0015:
        r2 = r4.mCurrentSize;	 Catch:{ all -> 0x002d }
        r3 = r0.length;	 Catch:{ all -> 0x002d }
        r2 = r2 - r3;
        r4.mCurrentSize = r2;	 Catch:{ all -> 0x002d }
        r2 = r4.mBuffersBySize;	 Catch:{ all -> 0x002d }
        r2.remove(r1);	 Catch:{ all -> 0x002d }
        r2 = r4.mBuffersByLastUse;	 Catch:{ all -> 0x002d }
        r2.remove(r0);	 Catch:{ all -> 0x002d }
    L_0x0025:
        monitor-exit(r4);
        return r0;
    L_0x0027:
        r1 = r1 + 1;
        goto L_0x0002;
    L_0x002a:
        r0 = new byte[r5];	 Catch:{ all -> 0x002d }
        goto L_0x0025;
    L_0x002d:
        r2 = move-exception;
        monitor-exit(r4);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.ByteArrayPool.getBuf(int):byte[]");
    }

    public synchronized void returnBuf(byte[] buf) {
        if (buf != null) {
            if (buf.length <= this.mSizeLimit) {
                this.mBuffersByLastUse.add(buf);
                int pos = Collections.binarySearch(this.mBuffersBySize, buf, BUF_COMPARATOR);
                if (pos < 0) {
                    pos = (-pos) - 1;
                }
                this.mBuffersBySize.add(pos, buf);
                this.mCurrentSize += buf.length;
                trim();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void trim() {
        /*
        r3 = this;
        monitor-enter(r3);
    L_0x0001:
        r1 = r3.mCurrentSize;	 Catch:{ all -> 0x001c }
        r2 = r3.mSizeLimit;	 Catch:{ all -> 0x001c }
        if (r1 <= r2) goto L_0x001f;
    L_0x0007:
        r1 = r3.mBuffersByLastUse;	 Catch:{ all -> 0x001c }
        r2 = 0;
        r0 = r1.remove(r2);	 Catch:{ all -> 0x001c }
        r0 = (byte[]) r0;	 Catch:{ all -> 0x001c }
        r1 = r3.mBuffersBySize;	 Catch:{ all -> 0x001c }
        r1.remove(r0);	 Catch:{ all -> 0x001c }
        r1 = r3.mCurrentSize;	 Catch:{ all -> 0x001c }
        r2 = r0.length;	 Catch:{ all -> 0x001c }
        r1 = r1 - r2;
        r3.mCurrentSize = r1;	 Catch:{ all -> 0x001c }
        goto L_0x0001;
    L_0x001c:
        r1 = move-exception;
        monitor-exit(r3);
        throw r1;
    L_0x001f:
        monitor-exit(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.ByteArrayPool.trim():void");
    }
}
