package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
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

public class login extends Activity {
    String err_flag;
    String names;
    String phone;
    String pic;
    String position;
    ProgressDialog progress;
    String txtPassword_;
    String txtUsername_;

    /* renamed from: info.cypherafrica.kefwa.login.1 */
    class C00671 implements Runnable {
        C00671() {
        }

        public void run() {
            login.this.progress = new ProgressDialog(login.this);
            login.this.progress.setTitle("Processing");
            login.this.progress.setMessage("Loging in");
            login.this.progress.show();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.login.2 */
    class C00682 implements Runnable {
        C00682() {
        }

        public void run() {
            ((EditText) login.this.findViewById(C0062R.id.txtRegUsername)).setError("Login Failed. Recheck details");
            Toast.makeText(login.this.getApplicationContext(), "Login failed", 0).show();
            login.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.login.3 */
    class C00693 implements Runnable {
        C00693() {
        }

        public void run() {
            Toast.makeText(login.this.getApplicationContext(), "Login successful", 0).show();
            login.this.progress.dismiss();
            Editor editor = login.this.getSharedPreferences("tbl_child", 0).edit();
            editor.putString("username", login.this.txtUsername_);
            editor.putString("pin", login.this.txtPassword_);
            editor.putString("names", login.this.names);
            editor.putString("phone", login.this.phone);
            editor.putString("position", login.this.position);
            editor.putString("pic", login.this.pic);
            if (((CheckBox) login.this.findViewById(C0062R.id.chkRemember)).isChecked()) {
                editor.putString("remember", "true");
                Toast.makeText(login.this.getApplicationContext(), "The system will remember you next time you login.", 1).show();
            }
            editor.commit();
            login.this.startActivity(new Intent(login.this.getApplicationContext(), home.class));
            login.this.finish();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.login.4 */
    class C00704 implements Runnable {
        C00704() {
        }

        public void run() {
            Toast.makeText(login.this.getApplicationContext(), "Error : " + login.this.err_flag, 0).show();
            login.this.progress.dismiss();
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
        private DownloadFilesTask() {
        }

        protected Long doInBackground(String... params) {
            login.this.login_online(params[0], params[1]);
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    public login() {
        this.names = "";
        this.pic = "";
        this.phone = "";
        this.position = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.login);
        Editor editor = getSharedPreferences("tbl_session_cred", 0).edit();
        if (getSharedPreferences("tbl_child", 0).getString("remember", null) != null) {
            startActivity(new Intent(getApplicationContext(), home.class));
            finish();
        }
    }

    public void regsiter(View v) {
        startActivity(new Intent(this, register.class));
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
        } else {
            new DownloadFilesTask().execute(new String[]{this.txtUsername_, this.txtPassword_});
        }
    }

    public void show_toast(String message) {
        Toast.makeText(getApplicationContext(), message, 0).show();
    }

    public void login_online(String txtUsername__, String txtPassword__) {
        new Handler(Looper.getMainLooper()).post(new C00671());
        String URL = new functions().get_url();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("request", "login"));
            nameValuePairs.add(new BasicNameValuePair("username", txtUsername__));
            nameValuePairs.add(new BasicNameValuePair("password", txtPassword__));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            this.err_flag = res.toString();
            Handler h;
            Handler handler;
            if (res.equals("N")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00682());
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
            String[] g = res.split("#");
            if (g.length <= 0) {
                return;
            }
            if (g[0].equals("Y")) {
                this.names = g[1];
                this.phone = g[2];
                this.position = g[3];
                this.pic = g[4];
                h = new Handler(Looper.getMainLooper());
                h.post(new C00693());
                handler = h;
                return;
            }
            h = new Handler(Looper.getMainLooper());
            h.post(new C00704());
            handler = h;
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }
}
