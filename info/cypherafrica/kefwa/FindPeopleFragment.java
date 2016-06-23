package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.android.volley.Cache.Entry;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import info.androidhive.listviewfeed.MainActivity;
import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindPeopleFragment extends Fragment {
    private static final String TAG;
    private String URL_FEED;
    public Activity ctx;
    private List<FeedItem> feedItems;
    private FeedListAdapter listAdapter;
    private ListView listView;
    ProgressBar pgHome;

    /* renamed from: info.cypherafrica.kefwa.FindPeopleFragment.1 */
    class C00991 implements Listener<JSONObject> {
        C00991() {
        }

        public void onResponse(JSONObject response) {
            VolleyLog.m0d(FindPeopleFragment.TAG, "Response: " + response.toString());
            if (response != null) {
                FindPeopleFragment.this.parseJsonFeed(response);
            }
        }
    }

    /* renamed from: info.cypherafrica.kefwa.FindPeopleFragment.2 */
    class C01002 implements ErrorListener {
        C01002() {
        }

        public void onErrorResponse(VolleyError error) {
            VolleyLog.m0d(FindPeopleFragment.TAG, "Error: " + error.getMessage());
        }
    }

    static {
        TAG = MainActivity.class.getSimpleName();
    }

    public FindPeopleFragment(Activity cont) {
        this.URL_FEED = "http://api.androidhive.info/feed/feed.json";
        this.ctx = cont;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0062R.layout.new_feed, container, false);
        this.listView = (ListView) rootView.findViewById(C0062R.id.list);
        this.feedItems = new ArrayList();
        Activity x = this.ctx;
        Entry entry = AppController.getInstance().getRequestQueue().getCache().get(this.URL_FEED);
        if (entry != null) {
            try {
                try {
                    parseJsonFeed(new JSONObject(new String(entry.data, "UTF-8")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        } else {
            AppController.getInstance().addToRequestQueue(new JsonObjectRequest(0, this.URL_FEED, null, new C00991(), new C01002()));
        }
        return rootView;
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            for (int i = 0; i < feedArray.length(); i++) {
                String image;
                String feedUrl;
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));
                if (feedObj.isNull("image")) {
                    image = null;
                } else {
                    image = feedObj.getString("image");
                }
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));
                if (feedObj.isNull("url")) {
                    feedUrl = null;
                } else {
                    feedUrl = feedObj.getString("url");
                }
                item.setUrl(feedUrl);
                this.feedItems.add(item);
            }
            this.listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
