package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import info.androidhive.listviewfeed.MainActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MyProfile extends Activity {
    private static final String IMAGE_DIRECTORY_NAME = "Cypher_Africa";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static Context ctx;
    final int CAMERA_CAPTURE;
    final int FILE_CAPTURE;
    final int PIC_CROP;
    Editor editor;
    String err_flag;
    private Uri fileUri;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    String names;
    ProgressBar pgHome;
    String phone;
    Bitmap photo;
    String pic;
    private Uri picUri;
    String pin;
    String position;
    ProgressDialog progress;
    SharedPreferences settings;
    String team;
    String txtPassword_;
    String txtUsername_;
    String username;

    /* renamed from: info.cypherafrica.kefwa.MyProfile.1 */
    class C00571 implements Runnable {
        C00571() {
        }

        public void run() {
            MyProfile.this.progress = new ProgressDialog(MyProfile.this);
            MyProfile.this.progress.setTitle("Updating");
            MyProfile.this.progress.setMessage("Please wait");
            MyProfile.this.progress.show();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MyProfile.2 */
    class C00582 implements Runnable {
        C00582() {
        }

        public void run() {
            ((EditText) MyProfile.this.findViewById(C0062R.id.txtProfUsername)).setError("Username does not exist");
            Toast.makeText(MyProfile.this.getApplicationContext(), "Username does not exist", 0).show();
            MyProfile.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MyProfile.3 */
    class C00593 implements Runnable {
        C00593() {
        }

        public void run() {
            Toast.makeText(MyProfile.this.getApplicationContext(), "Profile updated Complete", 0).show();
            MyProfile.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MyProfile.4 */
    class C00604 implements Runnable {
        C00604() {
        }

        public void run() {
            Toast.makeText(MyProfile.this.getApplicationContext(), "Error : " + MyProfile.this.err_flag, 0).show();
            MyProfile.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MyProfile.5 */
    class C00615 implements OnClickListener {
        private final /* synthetic */ CharSequence[] val$items;

        C00615(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public void onClick(DialogInterface dialog, int item) {
            if (this.val$items[item].equals("Take Photo")) {
                MyProfile.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), MyProfile.MEDIA_TYPE_IMAGE);
            } else if (this.val$items[item].equals("Choose from Library")) {
                Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                MyProfile.this.startActivityForResult(Intent.createChooser(intent, "Select File"), 20);
            } else if (this.val$items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    }

    private class UpdateProfileAsync extends AsyncTask<String, Integer, Long> {
        private UpdateProfileAsync() {
        }

        protected Long doInBackground(String... params) {
            MyProfile.this.update_online();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    public MyProfile() {
        this.names = "";
        this.pic = "";
        this.phone = "";
        this.CAMERA_CAPTURE = MEDIA_TYPE_IMAGE;
        this.FILE_CAPTURE = 20;
        this.PIC_CROP = MEDIA_TYPE_VIDEO;
        this.position = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.profile);
        ctx = this;
        SharedPreferences settings = getSharedPreferences("tbl_session_cred", 0);
        EditText username = (EditText) findViewById(C0062R.id.txtProfUsername);
        EditText names = (EditText) findViewById(C0062R.id.txtProfFullName);
        EditText phone = (EditText) findViewById(C0062R.id.txtProfPhone);
        EditText position = (EditText) findViewById(C0062R.id.txtProfPosition);
        EditText pin = (EditText) findViewById(C0062R.id.txtProfPIN);
        settings = getSharedPreferences("tbl_child", 0);
        username.setText(settings.getString("username", "NOT SET"));
        names.setText(settings.getString("names", "NOT SET"));
        phone.setText(settings.getString("phone", "NOT SET"));
        position.setText(settings.getString("position", "NOT SET"));
        pin.setText(settings.getString("pin", "NOT SET"));
        ((ImageView) findViewById(C0062R.id.imgProfile)).setImageBitmap(decodeBase64(settings.getString("pic", "NOT SET").toString()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0062R.id.events:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case C0062R.id.logout:
                this.editor = getSharedPreferences("tbl_child", 0).edit();
                this.editor.clear();
                this.editor.commit();
                startActivity(new Intent(this, login.class));
                finish();
                return true;
            case C0062R.id.profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.main, menu);
        return true;
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

    public void update_now(View v) {
        EditText txtUsername = (EditText) findViewById(C0062R.id.txtProfUsername);
        EditText txtPassword = (EditText) findViewById(C0062R.id.txtProfPIN);
        EditText txtPhone = (EditText) findViewById(C0062R.id.txtProfPhone);
        EditText txtNames = (EditText) findViewById(C0062R.id.txtProfFullName);
        EditText txtPosition = (EditText) findViewById(C0062R.id.txtProfPosition);
        EditText txtPIN = (EditText) findViewById(C0062R.id.txtProfPIN);
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
            new UpdateProfileAsync().execute(new String[0]);
        }
    }

    public void update_online() {
        new Handler(Looper.getMainLooper()).post(new C00571());
        String URL = new functions().get_url();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            String pic = encodeToBase64(this.photo);
            Editor editor = getSharedPreferences("tbl_child", 0).edit();
            editor.putString("pic", pic);
            editor.commit();
            List<NameValuePair> nameValuePairs = new ArrayList(MEDIA_TYPE_IMAGE);
            nameValuePairs.add(new BasicNameValuePair("request", "update_profile"));
            nameValuePairs.add(new BasicNameValuePair("username", this.username));
            nameValuePairs.add(new BasicNameValuePair("pin", this.pin));
            nameValuePairs.add(new BasicNameValuePair("phone", this.phone));
            nameValuePairs.add(new BasicNameValuePair("position", this.position));
            nameValuePairs.add(new BasicNameValuePair("self", "YES"));
            nameValuePairs.add(new BasicNameValuePair("agent", "NONE"));
            nameValuePairs.add(new BasicNameValuePair("names", this.names));
            nameValuePairs.add(new BasicNameValuePair("pic", pic));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            Handler h;
            Handler handler;
            if (res.equals("USERNAME")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00582());
                    handler = h;
                } catch (ClientProtocolException e) {
                    handler = h;
                } catch (IOException e2) {
                    handler = h;
                }
            } else if (res.trim().equals("SUCCESS")) {
                h = new Handler(Looper.getMainLooper());
                h.post(new C00593());
                handler = h;
            } else {
                this.err_flag = res.toString();
                h = new Handler(Looper.getMainLooper());
                h.post(new C00604());
                handler = h;
            }
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            return Base64.encodeToString(b, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        } catch (OutOfMemoryError e2) {
            baos = new ByteArrayOutputStream();
            image.compress(CompressFormat.JPEG, 50, baos);
            return Base64.encodeToString(baos.toByteArray(), 0);
        }
    }

    public void get_pic(View v) {
        selectImage();
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            if (type == MEDIA_TYPE_IMAGE) {
                return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            }
            return null;
        }
        Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create Cypher_Africa directory");
        return null;
    }

    private void selectImage() {
        CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};
        Builder builder = new Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new C00615(items));
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        if (requestCode == MEDIA_TYPE_IMAGE) {
            performCrop();
        } else if (requestCode == MEDIA_TYPE_VIDEO) {
            Bitmap thePic = (Bitmap) data.getExtras().getParcelable("data");
            this.photo = thePic;
            ((ImageView) findViewById(C0062R.id.imgProfile)).setImageBitmap(thePic);
        } else if (requestCode == 20) {
            Uri selectedImageUri = data.getData();
            String[] projection = new String[MEDIA_TYPE_IMAGE];
            projection[0] = "_data";
            Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            int scale = MEDIA_TYPE_IMAGE;
            while ((options.outWidth / scale) / MEDIA_TYPE_VIDEO >= 200 && (options.outHeight / scale) / MEDIA_TYPE_VIDEO >= 200) {
                scale *= MEDIA_TYPE_VIDEO;
            }
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
            ImageView picView = (ImageView) findViewById(C0062R.id.imgProfile);
            this.photo = bm;
            picView.setImageBitmap(bm);
        }
    }

    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(this.fileUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", MEDIA_TYPE_IMAGE);
            cropIntent.putExtra("aspectY", MEDIA_TYPE_IMAGE);
            cropIntent.putExtra("outputX", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
            cropIntent.putExtra("outputY", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, MEDIA_TYPE_VIDEO);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Whoops - your device doesn't support the crop action!", 0).show();
        }
    }
}
