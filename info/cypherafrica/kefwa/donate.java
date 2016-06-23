package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class donate extends Activity {
    static Context ctx;
    Editor editor;
    String err_flag;
    String girlsT;
    String names;
    String padsT;
    String pantsT;
    ProgressBar pgTopup;
    String phone;
    String pic;
    String position;
    ProgressDialog progress;
    String schoolT;
    SharedPreferences settings;
    String txtPassword_;
    String txtUsername_;
    String username;

    /* renamed from: info.cypherafrica.kefwa.donate.1 */
    class C00631 implements Runnable {
        C00631() {
        }

        public void run() {
        }
    }

    /* renamed from: info.cypherafrica.kefwa.donate.2 */
    class C00642 implements Runnable {
        C00642() {
        }

        public void run() {
            ((NotificationManager) donate.this.getSystemService("notification")).notify(1, new Builder(donate.ctx).setSmallIcon(C0062R.drawable.ic_launcher).setContentTitle("Donation").setContentText("Your donation has been recieved").build());
            Toast.makeText(donate.this.getApplicationContext(), "Your donation has been recieved successfully", 0).show();
            donate.this.startActivity(new Intent(donate.this.getApplicationContext(), home.class));
            donate.this.finish();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.donate.3 */
    class C00653 implements Runnable {
        C00653() {
        }

        public void run() {
            Toast.makeText(donate.ctx, "Error occured : " + donate.this.err_flag, 1).show();
        }
    }

    private class TopupAsync extends AsyncTask<String, Integer, Long> {
        private TopupAsync() {
        }

        protected Long doInBackground(String... params) {
            donate.this.topupOnline();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    public donate() {
        this.names = "";
        this.pic = "";
        this.phone = "";
        this.username = "";
        this.position = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.donate);
        ctx = this;
        Editor editor = getSharedPreferences("tbl_session_cred", 0).edit();
        this.username = getSharedPreferences("tbl_child", 0).getString("username", "EMPTY");
    }

    public void proceed(View v) {
        EditText school = (EditText) findViewById(C0062R.id.txtSchool);
        EditText pants = (EditText) findViewById(C0062R.id.txtPantsDonate);
        EditText pads = (EditText) findViewById(C0062R.id.txtPadsDonated);
        EditText girls = (EditText) findViewById(C0062R.id.txtGirlsImpacted);
        if (school.getText().toString().equals("")) {
            school.setError("Enter school name");
        } else if (pants.getText().toString().equals("")) {
            pants.setError("Enter Pants Donated");
        } else if (pads.getText().toString().equals("")) {
            pads.setError("Enter Pads Donated");
        } else if (girls.getText().toString().equals("")) {
            girls.setError("Enter Girls Impacted");
        } else {
            this.schoolT = school.getText().toString();
            this.pantsT = pants.getText().toString();
            this.padsT = pads.getText().toString();
            this.girlsT = girls.getText().toString();
            new TopupAsync().execute(new String[0]);
            Toast.makeText(this, "Processing your donation", 1).show();
        }
    }

    public void login(View v) {
        EditText txtUsername = (EditText) findViewById(C0062R.id.txtRegUsername);
        EditText txtPassword = (EditText) findViewById(C0062R.id.txtRegFullName);
        this.txtUsername_ = txtUsername.getText().toString();
        this.txtPassword_ = txtPassword.getText().toString();
        if (txtUsername.getText().toString().equals("")) {
            txtUsername.setError("Username Required");
        } else if (txtPassword.getText().toString().equals("")) {
            txtPassword.setError("Password Required");
        }
    }

    public void show_toast(String message) {
        Toast.makeText(getApplicationContext(), message, 0).show();
    }

    public void topupOnline() {
        new Handler(Looper.getMainLooper()).post(new C00631());
        String URL = new functions().get_url();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("request", "donate"));
            nameValuePairs.add(new BasicNameValuePair("school", this.schoolT));
            nameValuePairs.add(new BasicNameValuePair("pants", this.pantsT));
            nameValuePairs.add(new BasicNameValuePair("pads", this.padsT));
            nameValuePairs.add(new BasicNameValuePair("girls", this.girlsT));
            nameValuePairs.add(new BasicNameValuePair("donator", this.username));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            Log.d("TOPUP", res);
            this.err_flag = res.toString();
            Handler h;
            Handler handler;
            if (res.equals("Y")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00642());
                    handler = h;
                    return;
                } catch (ClientProtocolException e) {
                    handler = h;
                    return;
                } catch (IOException e2) {
                    handler = h;
                    return;
                }
            }
            h = new Handler(Looper.getMainLooper());
            h.post(new C00653());
            handler = h;
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        Bitmap bitmap = null;
        try {
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("Error While Decoding : ", e.getMessage());
            return BitmapFactory.decodeStream(ctx.getResources().openRawResource(C0062R.drawable.ic_launcher));
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("Error While Decoding : ", e2.getMessage());
            return bitmap;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0062R.id.logout:
                this.editor = getSharedPreferences("tbl_child", 0).edit();
                this.editor.clear();
                this.editor.commit();
                startActivity(new Intent(ctx, login.class));
                finish();
                return true;
            case C0062R.id.profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
