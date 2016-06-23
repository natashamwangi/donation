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

public class topup extends Activity {
    static Context ctx;
    String amount;
    String balance;
    String cardnumber;
    String cvv;
    Editor editor;
    String err_flag;
    String expirydate;
    String nameoncard;
    String names;
    ProgressBar pgTopup;
    String phone;
    String pic;
    String position;
    ProgressDialog progress;
    SharedPreferences settings;
    String txtPassword_;
    String txtUsername_;
    String username;

    /* renamed from: info.cypherafrica.kefwa.topup.1 */
    class C00761 implements Runnable {
        C00761() {
        }

        public void run() {
        }
    }

    /* renamed from: info.cypherafrica.kefwa.topup.2 */
    class C00772 implements Runnable {
        C00772() {
        }

        public void run() {
            ((NotificationManager) topup.this.getSystemService("notification")).notify(1, new Builder(topup.ctx).setSmallIcon(C0062R.drawable.ic_launcher).setContentTitle("Payment Complete").setContentText("Your giving has been recieved").build());
            Toast.makeText(topup.this.getApplicationContext(), "Your giving has been recieved successfully", 0).show();
            topup.this.pgTopup.setVisibility(4);
            topup.this.startActivity(new Intent(topup.this.getApplicationContext(), home.class));
            topup.this.finish();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.topup.3 */
    class C00783 implements Runnable {
        C00783() {
        }

        public void run() {
            Toast.makeText(topup.ctx, "Error occured : " + topup.this.err_flag, 1).show();
        }
    }

    private class TopupAsync extends AsyncTask<String, Integer, Long> {
        private TopupAsync() {
        }

        protected Long doInBackground(String... params) {
            topup.this.topupOnline();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    public topup() {
        this.names = "";
        this.pic = "";
        this.phone = "";
        this.username = "";
        this.position = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.topup);
        ctx = this;
        Editor editor = getSharedPreferences("tbl_session_cred", 0).edit();
        this.pgTopup = (ProgressBar) findViewById(C0062R.id.pgLogin);
        this.username = getSharedPreferences("tbl_child", 0).getString("username", "EMPTY");
    }

    public void proceed(View v) {
        EditText cardnumberE = (EditText) findViewById(C0062R.id.txtCardNumber);
        EditText nameoncardE = (EditText) findViewById(C0062R.id.txtNameOnCard);
        EditText amountE = (EditText) findViewById(C0062R.id.txtAmount);
        EditText cvvE = (EditText) findViewById(C0062R.id.txtCVV);
        EditText expirydateE = (EditText) findViewById(C0062R.id.txtExpiryDate);
        if (amountE.getText().toString().equals("")) {
            amountE.setError("Enter amount to topup");
        } else if (nameoncardE.getText().toString().equals("")) {
            nameoncardE.setError("Enter name on card");
        } else if (cardnumberE.getText().toString().equals("")) {
            cardnumberE.setError("Enter card number");
        } else if (cvvE.getText().toString().equals("")) {
            cvvE.setError("Enter CVV");
        } else if (expirydateE.getText().toString().equals("")) {
            expirydateE.setError("Enter Expiry Date");
        } else {
            this.amount = amountE.getText().toString();
            this.nameoncard = nameoncardE.getText().toString();
            this.cardnumber = cardnumberE.getText().toString();
            this.cvv = cvvE.getText().toString();
            this.expirydate = expirydateE.getText().toString();
            this.pgTopup.setVisibility(0);
            new TopupAsync().execute(new String[0]);
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
        new Handler(Looper.getMainLooper()).post(new C00761());
        String URL = new functions().get_url();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("request", "topup"));
            nameValuePairs.add(new BasicNameValuePair("amount", this.amount));
            nameValuePairs.add(new BasicNameValuePair("nameoncard", this.nameoncard));
            nameValuePairs.add(new BasicNameValuePair("cardnumber", this.cardnumber));
            nameValuePairs.add(new BasicNameValuePair("cvv", this.cvv));
            nameValuePairs.add(new BasicNameValuePair("expirydate", this.expirydate));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            Log.d("TOPUP", res);
            this.err_flag = res.toString();
            Handler h;
            Handler handler;
            if (res.equals("Y")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00772());
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
            h.post(new C00783());
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
