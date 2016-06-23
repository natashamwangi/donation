package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.CursorAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case DialogFragment.STYLE_NORMAL /*0*/:
            case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
            case FragmentManagerImpl.ANIM_STYLE_CLOSE_ENTER /*3*/:
            case TransportMediator.FLAG_KEY_MEDIA_PLAY /*4*/:
            case FragmentManagerImpl.ANIM_STYLE_FADE_ENTER /*5*/:
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT /*6*/:
                return true;
            case DefaultRetryPolicy.DEFAULT_MAX_RETRIES /*1*/:
            case Method.PATCH /*7*/:
            case WindowCompat.FEATURE_ACTION_BAR_OVERLAY /*9*/:
                return false;
            default:
                return true;
        }
    }
}
