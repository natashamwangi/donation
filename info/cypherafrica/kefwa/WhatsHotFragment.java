package info.cypherafrica.kefwa;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WhatsHotFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0062R.layout.fragment_whats_hot, container, false);
        WebView wv = (WebView) rootView.findViewById(C0062R.id.webDivisionOne);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("http://www.futaa.com/widget/news/division+one");
        return rootView;
    }
}
