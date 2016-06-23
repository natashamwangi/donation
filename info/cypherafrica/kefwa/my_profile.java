package info.cypherafrica.kefwa;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class my_profile extends Fragment {
    static Context f2c;

    public my_profile(Context ctx) {
        f2c = ctx;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0062R.layout.profile, container, false);
        SharedPreferences settings = f2c.getSharedPreferences("tbl_session_cred", 0);
        EditText username = (EditText) rootView.findViewById(C0062R.id.txtProfUsername);
        EditText names = (EditText) rootView.findViewById(C0062R.id.txtProfFullName);
        EditText phone = (EditText) rootView.findViewById(C0062R.id.txtProfPhone);
        EditText position = (EditText) rootView.findViewById(C0062R.id.txtProfPosition);
        EditText pin = (EditText) rootView.findViewById(C0062R.id.txtProfPIN);
        settings = f2c.getSharedPreferences("tbl_child", 0);
        username.setText(settings.getString("username", "NOT SET"));
        names.setText(settings.getString("names", "NOT SET"));
        phone.setText(settings.getString("phone", "NOT SET"));
        position.setText(settings.getString("position", "NOT SET"));
        pin.setText(settings.getString("pin", "NOT SET"));
        ((ImageView) rootView.findViewById(C0062R.id.imgProfile)).setImageBitmap(decodeBase64(settings.getString("pic", "NOT SET").toString()));
        return rootView;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        Bitmap bitmap = null;
        try {
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("Error While Decoding : ", e.getMessage());
            return BitmapFactory.decodeStream(f2c.getResources().openRawResource(C0062R.drawable.ic_launcher));
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("Error While Decoding : ", e2.getMessage());
            return bitmap;
        }
    }
}
