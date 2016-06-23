package info.cypherafrica.kefwa;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.WindowCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request.Method;
import info.cypherafrica.kefwa.adapter.NavDrawerListAdapter;
import info.cypherafrica.kefwa.model.NavDrawerItem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

public class MainActivityOld extends Activity {
    private static final String IMAGE_DIRECTORY_NAME = "Cypher_Africa";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    final int CAMERA_CAPTURE;
    String DOB;
    final int FILE_CAPTURE;
    final int PIC_CROP;
    private NavDrawerListAdapter adapter;
    String address;
    Context ctx;
    Editor editor;
    String err_flag;
    private Uri fileUri;
    String id_number;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    String marital;
    String names;
    String nationality;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private TypedArray navMenuIcons;
    private String[] navMenuTitles;
    String next_of_kin;
    String number_next_of_kin;
    String phone;
    Bitmap photo;
    private Uri picUri;
    String pin;
    String place_of_birth;
    String position;
    ProgressDialog progress;
    SharedPreferences settings;
    String team;
    String txtPassword_;
    String txtUsername_;
    String username;

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.2 */
    class C00492 implements OnClickListener {
        private final /* synthetic */ CharSequence[] val$items;

        C00492(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public void onClick(DialogInterface dialog, int item) {
            if (this.val$items[item].equals("Take Photo")) {
                MainActivityOld.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), MainActivityOld.MEDIA_TYPE_IMAGE);
            } else if (this.val$items[item].equals("Choose from Library")) {
                Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                MainActivityOld.this.startActivityForResult(Intent.createChooser(intent, "Select File"), 20);
            } else if (this.val$items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.3 */
    class C00503 implements Runnable {
        C00503() {
        }

        public void run() {
            MainActivityOld.this.progress = new ProgressDialog(MainActivityOld.this);
            MainActivityOld.this.progress.setTitle("Updating");
            MainActivityOld.this.progress.setMessage("Please wait");
            MainActivityOld.this.progress.show();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.4 */
    class C00514 implements Runnable {
        C00514() {
        }

        public void run() {
            ((EditText) MainActivityOld.this.findViewById(C0062R.id.txtProfUsername)).setError("Username does not exist");
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Username does not exist", 0).show();
            MainActivityOld.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.5 */
    class C00525 implements Runnable {
        C00525() {
        }

        public void run() {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Profile updated Complete", 0).show();
            MainActivityOld.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.6 */
    class C00536 implements Runnable {
        C00536() {
        }

        public void run() {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Error : " + MainActivityOld.this.err_flag, 0).show();
            MainActivityOld.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.7 */
    class C00547 implements Runnable {
        C00547() {
        }

        public void run() {
            MainActivityOld.this.progress = new ProgressDialog(MainActivityOld.this);
            MainActivityOld.this.progress.setTitle("Registering member");
            MainActivityOld.this.progress.setMessage("Please wait");
            MainActivityOld.this.progress.show();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.8 */
    class C00558 implements Runnable {
        C00558() {
        }

        public void run() {
            ((EditText) MainActivityOld.this.findViewById(C0062R.id.txtProfUsername)).setError("Username does not exist");
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Username does not exist", 0).show();
            MainActivityOld.this.progress.dismiss();
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.9 */
    class C00569 implements Runnable {
        C00569() {
        }

        public void run() {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Member has been saved", 0).show();
            MainActivityOld.this.progress.dismiss();
            MainActivityOld.this.displayView(0);
        }
    }

    class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        DownloadFilesTask() {
        }

        protected Long doInBackground(URL... urls) {
            return Long.valueOf(100);
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "On progress", 0).show();
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Finished", 0).show();
        }
    }

    private class RegisterMember extends AsyncTask<String, Integer, Long> {
        private RegisterMember() {
        }

        protected Long doInBackground(String... params) {
            MainActivityOld.this.register_member_online();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    private class SlideMenuClickListener implements OnItemClickListener {
        private SlideMenuClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MainActivityOld.this.displayView(position);
        }
    }

    class UpdateProfile extends AsyncTask<URL, Integer, Long> {
        UpdateProfile() {
        }

        protected Long doInBackground(URL... urls) {
            return Long.valueOf(100);
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "On progress", 0).show();
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(MainActivityOld.this.getApplicationContext(), "Finished", 0).show();
        }
    }

    private class UpdateProfileAsync extends AsyncTask<String, Integer, Long> {
        private UpdateProfileAsync() {
        }

        protected Long doInBackground(String... params) {
            MainActivityOld.this.update_online();
            return Long.valueOf(1);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    /* renamed from: info.cypherafrica.kefwa.MainActivityOld.1 */
    class C01041 extends ActionBarDrawerToggle {
        C01041(Activity $anonymous0, DrawerLayout $anonymous1, int $anonymous2, int $anonymous3, int $anonymous4) {
            super($anonymous0, $anonymous1, $anonymous2, $anonymous3, $anonymous4);
        }

        public void onDrawerClosed(View view) {
            MainActivityOld.this.getActionBar().setTitle(MainActivityOld.this.mTitle);
            MainActivityOld.this.invalidateOptionsMenu();
        }

        public void onDrawerOpened(View drawerView) {
            MainActivityOld.this.getActionBar().setTitle(MainActivityOld.this.mDrawerTitle);
            MainActivityOld.this.invalidateOptionsMenu();
        }
    }

    public MainActivityOld() {
        this.CAMERA_CAPTURE = MEDIA_TYPE_IMAGE;
        this.FILE_CAPTURE = 20;
        this.PIC_CROP = MEDIA_TYPE_VIDEO;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0062R.layout.activity_main);
        this.ctx = this;
        CharSequence title = getTitle();
        this.mDrawerTitle = title;
        this.mTitle = title;
        this.navMenuTitles = getResources().getStringArray(C0062R.array.nav_drawer_items);
        this.navMenuIcons = getResources().obtainTypedArray(C0062R.array.nav_drawer_icons);
        this.mDrawerLayout = (DrawerLayout) findViewById(C0062R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(C0062R.id.list_slidermenu);
        this.navDrawerItems = new ArrayList();
        this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[0], this.navMenuIcons.getResourceId(0, -1)));
        this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[MEDIA_TYPE_IMAGE], this.navMenuIcons.getResourceId(MEDIA_TYPE_IMAGE, -1)));
        this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[MEDIA_TYPE_VIDEO], this.navMenuIcons.getResourceId(MEDIA_TYPE_VIDEO, -1)));
        this.navMenuIcons.recycle();
        this.mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        this.adapter = new NavDrawerListAdapter(getApplicationContext(), this.navDrawerItems);
        this.mDrawerList.setAdapter(this.adapter);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        this.mDrawerToggle = new C01041(this, this.mDrawerLayout, C0062R.drawable.ic_drawer, C0062R.string.app_name, C0062R.string.app_name);
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0062R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case C0062R.id.logout:
                this.editor = getSharedPreferences("tbl_child", 0).edit();
                this.editor.clear();
                this.editor.commit();
                startActivity(new Intent(this, login.class));
                finish();
                return true;
            case C0062R.id.profile:
                displayView(10);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(C0062R.id.logout).setVisible(!this.mDrawerLayout.isDrawerOpen(this.mDrawerList));
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case DialogFragment.STYLE_NORMAL /*0*/:
                fragment = new HomeFragment();
                break;
            case MEDIA_TYPE_IMAGE /*1*/:
                fragment = new FindPeopleFragment(this);
                break;
            case MEDIA_TYPE_VIDEO /*2*/:
                fragment = new PhotosFragment();
                break;
            case FragmentManagerImpl.ANIM_STYLE_CLOSE_ENTER /*3*/:
                fragment = new CommunityFragment();
                break;
            case TransportMediator.FLAG_KEY_MEDIA_PLAY /*4*/:
                fragment = new PagesFragment();
                break;
            case FragmentManagerImpl.ANIM_STYLE_FADE_ENTER /*5*/:
                fragment = new WhatsHotFragment();
                break;
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT /*6*/:
                fragment = new News();
                break;
            case Method.PATCH /*7*/:
                fragment = new EventsandNews();
                break;
            case TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE /*8*/:
                fragment = new Tounaments();
                break;
            case WindowCompat.FEATURE_ACTION_BAR_OVERLAY /*9*/:
                fragment = new Membership();
                break;
            case WindowCompat.FEATURE_ACTION_MODE_OVERLAY /*10*/:
                fragment = new my_profile(this);
                break;
        }
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(C0062R.id.frame_container, fragment).commit();
            this.mDrawerList.setItemChecked(position, true);
            this.mDrawerList.setSelection(position);
            setTitle(this.navMenuTitles[position]);
            this.mDrawerLayout.closeDrawer(this.mDrawerList);
            return;
        }
        Log.e("MainActivity", "Error in creating fragment");
    }

    public void login(View v) {
        EditText txtUsername = (EditText) findViewById(C0062R.id.txtRegUsername);
        EditText txtPassword = (EditText) findViewById(C0062R.id.txtRegFullName);
        if (txtUsername.getText().toString().equals("")) {
            txtUsername.setError("Username Required");
        } else if (txtPassword.getText().toString().equals("")) {
            txtPassword.setError("Password Required");
        } else {
            new DownloadFilesTask().execute(new URL[0]);
        }
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        getActionBar().setTitle(this.mTitle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
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
            new DownloadFilesTask().execute(new URL[0]);
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
        builder.setItems(items, new C00492(items));
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

    public void register_member(View v) {
        EditText txtTeam = (EditText) findViewById(C0062R.id.txtRegPIN);
        EditText txtPhone = (EditText) findViewById(C0062R.id.txtRegPhone);
        EditText txtNames = (EditText) findViewById(C0062R.id.txtRegFullName);
        EditText txtPosition = (EditText) findViewById(C0062R.id.txtRegPosition);
        EditText txtPIN = (EditText) findViewById(C0062R.id.txtRegPIN);
        EditText txtIDNumber = (EditText) findViewById(C0062R.id.txtMEMIDNumber);
        EditText txtDOB = (EditText) findViewById(C0062R.id.txtMEMDOB);
        EditText txtPlaceOfBirth = (EditText) findViewById(C0062R.id.txtMEMPlaceOfBirth);
        EditText txtAddress = (EditText) findViewById(C0062R.id.txtMEMAddress);
        Spinner txtMarital = (Spinner) findViewById(C0062R.id.txtMEMMarital);
        EditText txtNationality = (EditText) findViewById(C0062R.id.txtMEMNationality);
        EditText txtNextOfKin = (EditText) findViewById(C0062R.id.txtMEMNextOfKin);
        EditText txtNextOfKinPhone = (EditText) findViewById(C0062R.id.txtMEMNextOfKinPhone);
        this.txtPassword_ = txtTeam.getText().toString();
        if (txtNames.getText().toString().equals("")) {
            txtNames.setError("Name Required");
        } else if (txtTeam.getText().toString().equals("")) {
            txtTeam.setError("Team Required");
        } else if (txtPhone.getText().toString().equals("")) {
            txtPhone.setError("Phone Number Required");
        } else {
            this.pin = txtTeam.getText().toString();
            this.phone = txtPhone.getText().toString();
            this.names = txtNames.getText().toString();
            this.position = txtPosition.getText().toString();
            this.DOB = txtDOB.getText().toString();
            this.id_number = txtIDNumber.getText().toString();
            this.place_of_birth = txtPlaceOfBirth.getText().toString();
            this.address = txtAddress.getText().toString();
            this.marital = txtMarital.getItemAtPosition(txtMarital.getSelectedItemPosition()).toString();
            this.nationality = txtNationality.getText().toString();
            this.next_of_kin = txtNextOfKin.getText().toString();
            this.number_next_of_kin = txtNextOfKinPhone.getText().toString();
            new RegisterMember().execute(new String[0]);
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

    public void show_toast(String message) {
        Toast.makeText(getApplicationContext(), message, 0).show();
    }

    public void update_online() {
        new Handler(Looper.getMainLooper()).post(new C00503());
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
                    h.post(new C00514());
                    handler = h;
                } catch (ClientProtocolException e) {
                    handler = h;
                } catch (IOException e2) {
                    handler = h;
                }
            } else if (res.trim().equals("SUCCESS")) {
                h = new Handler(Looper.getMainLooper());
                h.post(new C00525());
                handler = h;
            } else {
                this.err_flag = res.toString();
                h = new Handler(Looper.getMainLooper());
                h.post(new C00536());
                handler = h;
            }
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }

    public void register_member_online() {
        new Handler(Looper.getMainLooper()).post(new C00547());
        String URL = new functions().get_url();
        String pic = encodeToBase64(this.photo);
        getSharedPreferences("tbl_child", 0).edit().putString("pic", pic);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(new StringBuilder(String.valueOf(URL)).append("up.php").toString());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(MEDIA_TYPE_IMAGE);
            nameValuePairs.add(new BasicNameValuePair("request", "membership"));
            nameValuePairs.add(new BasicNameValuePair("team", this.pin));
            nameValuePairs.add(new BasicNameValuePair("phone", this.phone));
            nameValuePairs.add(new BasicNameValuePair("position", this.position));
            nameValuePairs.add(new BasicNameValuePair("self", "YES"));
            nameValuePairs.add(new BasicNameValuePair("agent", "NONE"));
            nameValuePairs.add(new BasicNameValuePair("names", this.names));
            nameValuePairs.add(new BasicNameValuePair("DOB", this.DOB));
            nameValuePairs.add(new BasicNameValuePair("id_number", this.id_number));
            nameValuePairs.add(new BasicNameValuePair("place_of_birth", this.place_of_birth));
            nameValuePairs.add(new BasicNameValuePair("marital", this.marital));
            nameValuePairs.add(new BasicNameValuePair("nationality", this.nationality));
            nameValuePairs.add(new BasicNameValuePair("next_of_kin", this.next_of_kin));
            nameValuePairs.add(new BasicNameValuePair("number_next_of_kin", this.number_next_of_kin));
            nameValuePairs.add(new BasicNameValuePair("pic", pic));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String res = EntityUtils.toString(httpclient.execute(httppost).getEntity()).trim();
            Handler h;
            Handler handler;
            if (res.equals("USERNAME")) {
                h = new Handler(Looper.getMainLooper());
                try {
                    h.post(new C00558());
                    handler = h;
                } catch (ClientProtocolException e) {
                    handler = h;
                } catch (IOException e2) {
                    handler = h;
                }
            } else if (res.trim().equals("SUCCESS")) {
                h = new Handler(Looper.getMainLooper());
                h.post(new C00569());
                handler = h;
            } else {
                this.err_flag = res.toString();
                h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivityOld.this.getApplicationContext(), "Error : " + MainActivityOld.this.err_flag, 0).show();
                        MainActivityOld.this.progress.dismiss();
                    }
                });
                handler = h;
            }
        } catch (ClientProtocolException e3) {
        } catch (IOException e4) {
        }
    }
}
