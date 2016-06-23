package info.cypherafrica.kefwa;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import java.io.File;

public class splash_screen extends Activity {
    private static int SPLASH_TIME_OUT;
    public SQLiteDatabase db;

    /* renamed from: info.cypherafrica.kefwa.splash_screen.1 */
    class C00751 implements Runnable {
        C00751() {
        }

        public void run() {
            splash_screen.this.startActivity(new Intent(splash_screen.this, login.class));
            splash_screen.this.finish();
        }
    }

    static {
        SPLASH_TIME_OUT = 2000;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.splash);
        try {
            this.db = openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + "cypher_africa", 0, null);
            this.db.execSQL("CREATE TABLE IF NOT EXISTS manifesto(id VARCHAR, content VARCHAR);");
            this.db.execSQL("CREATE TABLE IF NOT EXISTS biography(id VARCHAR, content VARCHAR);");
            Log.d("DB Created", "DB Created");
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }
        new Handler().postDelayed(new C00751(), (long) SPLASH_TIME_OUT);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.main, menu);
        return true;
    }
}
