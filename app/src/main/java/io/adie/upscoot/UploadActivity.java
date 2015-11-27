package io.adie.upscoot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

import io.adie.upscoot.tables.UploadedImage;

public class UploadActivity extends AppCompatActivity implements UploadPrivacyFragment.UploadPrivacyDialogListener {
    public static final String UPLOAD_URL = "https://i.sc0tt.net";
    public static final String FILE_FORM = "file";
    public static final String UPLOAD_PASS = "cake";
    private static final String TAG = UploadActivity.class.getSimpleName();
    SharedPreferences sharedPref;
    Intent currentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentIntent = getIntent();
        Uri imageUri = (Uri) currentIntent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.d(TAG, "onCreate Uri: " + imageUri.toString());

        String uploadPref = sharedPref.getString("upload_type", "0");
        Log.d(TAG, String.format("Upload pref: %s", uploadPref));

        if (uploadPref.equals("0")) {
            UploadPrivacyFragment frag = new UploadPrivacyFragment();
            frag.show(getSupportFragmentManager(), frag.getTag());
        } else {
            currentIntent.putExtra("private", uploadPref.equals("2"));
            uploadImage();
        }
    }

    private void uploadImage() {
        ContentResolver contentResolver = getContentResolver();

        Uri imageUri = (Uri) currentIntent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.d(TAG, "uploadImage Uri: " + imageUri.toString());

        String[] proj = {MediaStore.Images.Media.DATA};
        Uri qry;
        String sel;
        String[] args;

        try {
            String wholeID = DocumentsContract.getDocumentId(imageUri);
            String id = wholeID.split(":")[1];
            sel = MediaStore.Images.Media._ID + "=?";
            qry = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            args = new String[]{id};
        } catch (IllegalArgumentException ex) {
            qry = imageUri;
            sel = null;
            args = null;
        }

        Cursor cursor = contentResolver.query(qry, proj, sel, args, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String filePath = cursor.getString(column_index);
        cursor.close();

        File tempFile = new File(filePath);

        Future<JsonObject> uploading = Ion.with(getApplicationContext())
                .load(UPLOAD_URL)
                .setMultipartFile(FILE_FORM, tempFile)
                .setMultipartParameter("pass", UPLOAD_PASS)
                .setMultipartParameter("url", "")
                .setMultipartParameter("json", "1")
                .setMultipartParameter(currentIntent.getBooleanExtra("private", false) ? "h" : "", currentIntent.getBooleanExtra("private", false) ? "1" : "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String url = result.get("url").getAsString();
                        Log.i(TAG, String.format("File uploaded: %s", url));

                        boolean autoCopy = sharedPref.getBoolean("auto_copy", true);
                        Log.d(TAG, String.format("Auto copying: %s", autoCopy ? "true" : "false"));

                        if (autoCopy) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(getResources().getString(R.string.package_copy), url);
                            clipboard.setPrimaryClip(clip);
                        }

                        UploadedImage img = new UploadedImage(url, currentIntent.getBooleanExtra("private", false));
                        img.save();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.uploaded_toast_text), Toast.LENGTH_SHORT).show();
                    }
                });

        finish();
    }


    @Override
    public void onPrivacySelected(boolean hidden) {
        currentIntent.putExtra("private", hidden);
        uploadImage();
    }

}
