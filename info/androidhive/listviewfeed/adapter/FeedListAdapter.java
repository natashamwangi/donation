package info.androidhive.listviewfeed.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import info.androidhive.listviewfeed.FeedImageView;
import info.androidhive.listviewfeed.FeedImageView.ResponseObserver;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;
import info.cypherafrica.kefwa.C0062R;
import java.util.List;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader;
    private LayoutInflater inflater;

    /* renamed from: info.androidhive.listviewfeed.adapter.FeedListAdapter.1 */
    class C00981 implements ResponseObserver {
        C00981() {
        }

        public void onError() {
        }

        public void onSuccess() {
        }
    }

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.imageLoader = AppController.getInstance().getImageLoader();
        this.activity = activity;
        this.feedItems = feedItems;
    }

    public int getCount() {
        return this.feedItems.size();
    }

    public Object getItem(int location) {
        return this.feedItems.get(location);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(C0062R.layout.feed_item, null);
        }
        if (this.imageLoader == null) {
            this.imageLoader = AppController.getInstance().getImageLoader();
        }
        TextView timestamp = (TextView) convertView.findViewById(C0062R.id.timestamp);
        TextView statusMsg = (TextView) convertView.findViewById(C0062R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(C0062R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(C0062R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(C0062R.id.feedImage1);
        FeedItem item = (FeedItem) this.feedItems.get(position);
        ((TextView) convertView.findViewById(C0062R.id.name)).setText(item.getName());
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(item.getTimeStamp()), System.currentTimeMillis(), 1000);
        timestamp.setText(item.getTimeStamp());
        if (TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setVisibility(8);
        } else {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(0);
        }
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">" + item.getUrl() + "</a> "));
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(0);
        } else {
            url.setVisibility(8);
        }
        profilePic.setImageUrl(item.getProfilePic(), this.imageLoader);
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), this.imageLoader);
            feedImageView.setVisibility(0);
            feedImageView.setResponseObserver(new C00981());
        } else {
            feedImageView.setVisibility(8);
        }
        return convertView;
    }
}
