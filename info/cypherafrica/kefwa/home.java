package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import info.androidhive.listviewfeed.MainActivity;

public class home extends Activity {
    Editor editor;
    String err_flag;
    String names;
    ProgressBar pgHome;
    String phone;
    String pic;
    String position;
    ProgressDialog progress;
    SharedPreferences settings;
    String txtPassword_;
    String txtUsername_;

    /* renamed from: info.cypherafrica.kefwa.home.1 */
    class C00661 extends WebChromeClient {
        C00661() {
        }

        public void onProgressChanged(WebView view, int progress) {
            home.this.pgHome.setProgress(progress);
            if (progress == 100) {
                home.this.pgHome.setVisibility(4);
            }
        }
    }

    public home() {
        this.names = "";
        this.pic = "";
        this.phone = "";
        this.position = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.fragment_home);
        WebView wv = (WebView) findViewById(C0062R.id.webWelcome);
        Editor editor = getSharedPreferences("tbl_child", 0).edit();
        wv.getSettings().setJavaScriptEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        this.pgHome = (ProgressBar) findViewById(C0062R.id.pgHome);
        this.pgHome.setProgress(0);
        this.pgHome.setMax(100);
        wv.setWebChromeClient(new C00661());
        wv.loadUrl("file:///android_asset/smartphone/index.html");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0062R.id.donate:
                startActivity(new Intent(this, donate.class));
                return true;
            case C0062R.id.events:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case C0062R.id.logout:
                this.editor = getSharedPreferences("tbl_child", 0).edit();
                this.editor.clear();
                this.editor.commit();
                startActivity(new Intent(this, login.class));
                finish();
                return true;
            case C0062R.id.profile:
                startActivity(new Intent(this, MyProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.main, menu);
        return true;
    }
}
