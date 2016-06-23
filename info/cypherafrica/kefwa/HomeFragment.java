package info.cypherafrica.kefwa;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class HomeFragment extends Fragment {
    ProgressBar pgHome;

    /* renamed from: info.cypherafrica.kefwa.HomeFragment.1 */
    class C00481 extends WebChromeClient {
        C00481() {
        }

        public void onProgressChanged(WebView view, int progress) {
            HomeFragment.this.pgHome.setProgress(progress);
            if (progress == 100) {
                HomeFragment.this.pgHome.setVisibility(4);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0062R.layout.fragment_home, container, false);
        WebView wv = (WebView) rootView.findViewById(C0062R.id.webWelcome);
        wv.getSettings().setJavaScriptEnabled(true);
        this.pgHome = (ProgressBar) rootView.findViewById(C0062R.id.pgHome);
        this.pgHome.setProgress(0);
        this.pgHome.setMax(100);
        wv.setWebChromeClient(new C00481());
        wv.loadUrl("file:///android_asset/smartphone/index.html");
        return rootView;
    }
}
