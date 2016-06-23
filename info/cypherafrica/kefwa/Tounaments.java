package info.cypherafrica.kefwa;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class Tounaments extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0062R.layout.fragment_tounaments, container, false);
        WebView wv = (WebView) rootView.findViewById(C0062R.id.webTounaments);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setPluginState(PluginState.ON);
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.loadUrl("http://kefwa.com/?page_id=863");
        return rootView;
    }
}
