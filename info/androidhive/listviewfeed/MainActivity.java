package info.androidhive.listviewfeed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Cache.Entry;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import info.androidhive.listviewfeed.ShakeEventManager.ShakeListener;
import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;
import info.cypherafrica.kefwa.C0062R;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements ShakeListener {
    private static final String TAG;
    private String URL_FEED;
    private List<FeedItem> feedItems;
    private FeedListAdapter listAdapter;
    private ListView listView;
    private ShakeEventManager sd;

    /* renamed from: info.androidhive.listviewfeed.MainActivity.1 */
    class C00941 implements Listener<JSONObject> {
        C00941() {
        }

        public void onResponse(JSONObject response) {
            VolleyLog.m0d(MainActivity.TAG, "Response: " + response.toString());
            if (response != null) {
                MainActivity.this.parseJsonFeed(response);
            }
        }
    }

    /* renamed from: info.androidhive.listviewfeed.MainActivity.2 */
    class C00952 implements ErrorListener {
        C00952() {
        }

        public void onErrorResponse(VolleyError error) {
            VolleyLog.m0d(MainActivity.TAG, "Error: " + error.getMessage());
        }
    }

    public MainActivity() {
        this.URL_FEED = "http://cypherafrica.com/donation/events_json.php";
    }

    static {
        TAG = MainActivity.class.getSimpleName();
    }

    @SuppressLint({"NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.new_feed);
        this.listView = (ListView) findViewById(C0062R.id.list);
        this.feedItems = new ArrayList();
        this.listAdapter = new FeedListAdapter(this, this.feedItems);
        this.listView.setAdapter(this.listAdapter);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        Entry entry = AppController.getInstance().getRequestQueue().getCache().get(this.URL_FEED);
        this.sd = new ShakeEventManager();
        this.sd.setListener(this);
        this.sd.init(this);
        if (entry != null) {
            try {
                try {
                    parseJsonFeed(new JSONObject(new String(entry.data, "UTF-8")));
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                return;
            }
        }
        load_new_data();
    }

    private void load_new_data() {
        this.sd.deregister();
        AppController.getInstance().addToRequestQueue(new JsonObjectRequest(0, this.URL_FEED, null, new C00941(), new C00952()));
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            this.feedItems.clear();
            this.listAdapter.notifyDataSetChanged();
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
            this.sd.register();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.feed_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0062R.id.refresh:
                Toast.makeText(getApplicationContext(), "Refreshing", 0).show();
                load_new_data();
                return true;
            case C0062R.id.profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onShake() {
        Toast.makeText(getApplicationContext(), "Refreshing..", 0).show();
        load_new_data();
    }

    protected void onResume() {
        super.onResume();
        this.sd.register();
    }

    protected void onPause() {
        super.onPause();
        this.sd.deregister();
    }
}
