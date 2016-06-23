package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
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

public class register extends Activity {
    String err_flag;
    String names;
    String phone;
    String pin;
    String position;
    ProgressDialog progress;
    String txtPassword_;
    String txtUsername_;
    String username;

    /* renamed from: info.cypherafrica.kefwa.register.1 */
    class C00711 implements Runnable {
        C00711() {
        }

        public void run() {
            register.this.progress = new ProgressDialog(register.this);
            register.this.progress.setTitle("Registering");
            register.this.progress.setMessage("Please wait");
            register.this.progress.show();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.register.2 */
    class C00722 implements Runnable {
        C00722() {
        }

        public void run() {
            ((EditText) register.this.findViewById(C0062R.id.txtRegUsername)).setError("Username is in use");
            Toast.makeText(register.this.getApplicationContext(), "Username is in use", 0).show();
            register.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.register.3 */
    class C00733 implements Runnable {
        C00733() {
        }

        public void run() {
            Toast.makeText(register.this.getApplicationContext(), "Registration Complete", 0).show();
            register.this.progress.dismiss();
            register.this.startActivity(new Intent(register.this.getApplicationContext(), login.class));
            register.this.finish();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.register.4 */
    class C00744 implements Runnable {
        C00744() {
        }

        public void run() {
            Toast.makeText(register.this.getApplicationContext(), "Error : " + register.this.err_flag, 0).show();
            register.this.progress.dismiss();
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
        private DownloadFilesTask() {
        }

        protected Long doInBackground(String... params) {
            String txtUsername_ = params[0];
            String txtPassword = params[1];
            register.this.register_online();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.register);
    }

    public void register_now(View v) {
        EditText txtUsername = (EditText) findViewById(C0062R.id.txtRegUsername);
        EditText txtPassword = (EditText) findViewById(C0062R.id.txtRegPIN);
        EditText txtPhone = (EditText) findViewById(C0062R.id.txtRegPhone);
        EditText txtNames = (EditText) findViewById(C0062R.id.txtRegFullName);
        EditText txtPosition = (EditText) findViewById(C0062R.id.txtRegPosition);
        EditText txtPIN = (EditText) findViewById(C0062R.id.txtRegPIN);
        this.txtUsername_ = txtUsername.getText().toString();
        this.txtPassword_ = txtPassword.getText().toString();
        if (txtUsername.getText().toString().equals("")) {
            txtUsername.setError("Username Required");
        } else if (txtNames.getText().toString().equals("")) {
            txtNames.setError("Name Required");
        } else if (txtPassword.getText().toString().equals("")) {
            txtPassword.setError("PIN Required");
        } else if (txtPhone.getText().toString().equals("")) {
            txtPhone.setError("Phone Number Required");
        } else {
            this.username = txtUsername.getText().toString();
            this.pin = txtPIN.getText().toString();
            this.phone = txtPhone.getText().toString();
            this.names = txtNames.getText().toString();
            this.position = txtPosition.getText().toString();
            new DownloadFilesTask().execute(new String[]{this.txtUsername_, this.txtPassword_});
        }
    }

    public void show_toast(String message) {
        Toast.makeText(getApplicationContext(), message, 0).show();
    }

    public void register_online() {
        Handler h;
        Handler handler;
        new Handler(Looper.getMainLooper()).post(new C00711());
        String URL = new functions().get_url();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("request", "registration"));
            nameValuePairs.add(new BasicNameValuePair("username", this.username));
            nameValuePairs.add(new BasicNameValuePair("pin", this.pin));
            nameValuePairs.add(new BasicNameValuePair("phone", this.phone));
            nameValuePairs.add(new BasicNameValuePair("position", this.position));
            nameValuePairs.add(new BasicNameValuePair("self", "YES"));
            nameValuePairs.add(new BasicNameValuePair("agent", "NONE"));
            nameValuePairs.add(new BasicNameValuePair("names", this.names));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            if (res.equals("USERNAME")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00722());
                    handler = h;
                } catch (ClientProtocolException e) {
                    handler = h;
                } catch (IOException e2) {
                    handler = h;
                }
            } else if (res.trim().equals("SUCCESS")) {
                h = new Handler(Looper.getMainLooper());
                h.post(new C00733());
                handler = h;
            } else {
                this.err_flag = res.toString();
                h = new Handler(Looper.getMainLooper());
                h.post(new C00744());
                handler = h;
            }
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }
}
