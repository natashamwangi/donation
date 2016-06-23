package info.androidhive.listviewfeed;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class FeedImageView extends ImageView {
    private int mDefaultImageId;
    private int mErrorImageId;
    private ImageContainer mImageContainer;
    private ImageLoader mImageLoader;
    private ResponseObserver mObserver;
    private String mUrl;

    public interface ResponseObserver {
        void onError();

        void onSuccess();
    }

    /* renamed from: info.androidhive.listviewfeed.FeedImageView.1 */
    class C01031 implements ImageListener {
        private final /* synthetic */ boolean val$isInLayoutPass;

        /* renamed from: info.androidhive.listviewfeed.FeedImageView.1.1 */
        class C00471 implements Runnable {
            private final /* synthetic */ ImageContainer val$response;

            C00471(ImageContainer imageContainer) {
                this.val$response = imageContainer;
            }

            public void run() {
                C01031.this.onResponse(this.val$response, false);
            }
        }

        C01031(boolean z) {
            this.val$isInLayoutPass = z;
        }

        public void onErrorResponse(VolleyError error) {
            if (FeedImageView.this.mErrorImageId != 0) {
                FeedImageView.this.setImageResource(FeedImageView.this.mErrorImageId);
            }
            if (FeedImageView.this.mObserver != null) {
                FeedImageView.this.mObserver.onError();
            }
        }

        public void onResponse(ImageContainer response, boolean isImmediate) {
            if (isImmediate && this.val$isInLayoutPass) {
                FeedImageView.this.post(new C00471(response));
                return;
            }
            if (response.getBitmap() != null) {
                FeedImageView.this.setImageBitmap(response.getBitmap());
                FeedImageView.this.adjustImageAspect(response.getBitmap().getWidth(), response.getBitmap().getHeight());
            } else if (FeedImageView.this.mDefaultImageId != 0) {
                FeedImageView.this.setImageResource(FeedImageView.this.mDefaultImageId);
            }
            if (FeedImageView.this.mObserver != null) {
                FeedImageView.this.mObserver.onSuccess();
            }
        }
    }

    public void setResponseObserver(ResponseObserver observer) {
        this.mObserver = observer;
    }

    public FeedImageView(Context context) {
        this(context, null);
    }

    public FeedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url, ImageLoader imageLoader) {
        this.mUrl = url;
        this.mImageLoader = imageLoader;
        loadImageIfNecessary(false);
    }

    public void setDefaultImageResId(int defaultImage) {
        this.mDefaultImageId = defaultImage;
    }

    public void setErrorImageResId(int errorImage) {
        this.mErrorImageId = errorImage;
    }

    private void loadImageIfNecessary(boolean isInLayoutPass) {
        int width = getWidth();
        int height = getHeight();
        boolean isFullyWrapContent = getLayoutParams() != null && getLayoutParams().height == -2 && getLayoutParams().width == -2;
        if (width != 0 || height != 0 || isFullyWrapContent) {
            if (TextUtils.isEmpty(this.mUrl)) {
                if (this.mImageContainer != null) {
                    this.mImageContainer.cancelRequest();
                    this.mImageContainer = null;
                }
                setDefaultImageOrNull();
                return;
            }
            if (!(this.mImageContainer == null || this.mImageContainer.getRequestUrl() == null)) {
                if (!this.mImageContainer.getRequestUrl().equals(this.mUrl)) {
                    this.mImageContainer.cancelRequest();
                    setDefaultImageOrNull();
                } else {
                    return;
                }
            }
            this.mImageContainer = this.mImageLoader.get(this.mUrl, new C01031(isInLayoutPass));
        }
    }

    private void setDefaultImageOrNull() {
        if (this.mDefaultImageId != 0) {
            setImageResource(this.mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    protected void onDetachedFromWindow() {
        if (this.mImageContainer != null) {
            this.mImageContainer.cancelRequest();
            setImageBitmap(null);
            this.mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private void adjustImageAspect(int bWidth, int bHeight) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (bWidth != 0 && bHeight != 0) {
            int swidth = getWidth();
            int new_height = (swidth * bHeight) / bWidth;
            params.width = swidth;
            params.height = new_height;
            setLayoutParams(params);
        }
    }
}
