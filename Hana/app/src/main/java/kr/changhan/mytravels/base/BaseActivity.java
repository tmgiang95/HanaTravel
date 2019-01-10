package kr.changhan.mytravels.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.utils.ImageViewerDialog;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private Uri mCurrentPhotoUri;

    protected Uri getCropImagePath() {
        return mCurrentPhotoUri;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    /**
     * Displays a common alert dialog with Ok and Cancel buttons.
     *
     * @param titleId             title string resource id
     * @param messageId           message string resource id
     * @param okClickListener     the callback when the ok button is clicked
     * @param cancelClickListener the callback when the cancel button is clicked
     */
    protected void showAlertOkCancel(@StringRes int titleId
            , @StringRes int messageId
            , final DialogInterface.OnClickListener okClickListener
            , final DialogInterface.OnClickListener cancelClickListener) {
        new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(android.R.string.ok, okClickListener)
                .setNegativeButton(android.R.string.cancel, cancelClickListener)
                .show();
    }

    protected void showPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), MyConst.REQCD_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this,
                    e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.
                            errorCode);
            Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showPlaceAutoComplete() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this);
            startActivityForResult(intent, MyConst.REQCD_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this,
                    e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.
                            errorCode);
            Log.e(TAG, message, e);
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    message, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showImageViewer(String imgUri, String title, String subtitle, String desc) {
        Bundle b = new Bundle();
        b.putString(MyConst.KEY_ID, imgUri);
        b.putString(MyConst.KEY_TITLE, title);
        b.putString(MyConst.KEY_SUBTITLE, subtitle);
        b.putString(MyConst.KEY_DESC, desc);
        ImageViewerDialog dialog = new ImageViewerDialog();
        dialog.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, ImageViewerDialog.TAG);
    }

    protected void postRequestPermissionResult(final int reqCd, final boolean result) {
        Log.d(TAG, "postRequestPermissionResult: " + reqCd + " " + result);
    }

    protected void requestPermission(final int reqCd) {
        switch (reqCd) {
            case MyConst.REQCD_ACCESS_GALLERY:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionResult(reqCd, true);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showAlertOkCancel(R.string.permission_dialog_title,
                                R.string.permission_camera_gallery, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(BaseActivity.this,
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCd);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postRequestPermissionResult(reqCd, false);
                                    }
                                });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCd);
                    }
                }
                break;
            case MyConst.REQCD_ACCESS_CAMERA:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionResult(reqCd, true);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showAlertOkCancel(R.string.permission_dialog_title, R.string.permission_camera_msg,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(BaseActivity.this,
                                                new String[]{Manifest.permission.CAMERA,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCd);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postRequestPermissionResult(reqCd, false);
                                    }
                                });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, reqCd);
                    }
                }
                break;
            case MyConst.REQCD_ACCESS_FINE_LOCATION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionResult(reqCd, true);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showAlertOkCancel(R.string.permission_dialog_title, R.string.permission_camera_msg,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(BaseActivity.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, reqCd);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postRequestPermissionResult(reqCd, false);
                                    }
                                });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, reqCd);
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionResult(requestCode, false);
                    return;
                }
            }
            postRequestPermissionResult(requestCode, true);
            return;
        }
        postRequestPermissionResult(requestCode, false);
    }

    protected void takePhotoFromGallery() {
        mCurrentPhotoUri = null;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (intent.resolveActivity(getPackageManager()) == null) {
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "Your device does not have a gallery",
                    Snackbar.LENGTH_LONG).show();
            return;
        }
        startActivityForResult(intent, MyConst.REQCD_IMAGE_GALLERY);
    }

    protected void takePhotoFromCamera() {
        mCurrentPhotoUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            String imageFileName = "IMG_" + new SimpleDateFormat("yyyyMMd_dHHmmss").format(new Date());
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            mCurrentPhotoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (intent.resolveActivity(getPackageManager()) == null) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Your device does not have a camera",
                        Snackbar.LENGTH_LONG).show();
                return;
            }
            startActivityForResult(intent, MyConst.REQCD_IMAGE_CAMERA);
        } catch (IOException e) {
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "Cannot create an image file",
                    Snackbar.LENGTH_LONG).show();
            return;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) storageDir.mkdirs();
        File image = File.createTempFile(imageFileName,
                ".jpg",
                storageDir);
        mCurrentPhotoUri = Uri.fromFile(image);
        return image;
    }

    protected void cropImage(Uri srcUri) {
        mCurrentPhotoUri = null;
        if (srcUri == null) return;
        try {
            File outputFile = createImageFile();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(srcUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("outputX", 1080);
            intent.putExtra("outputY", 1080);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("max-width", 1080);
            intent.putExtra("max-height", 1080);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, MyConst.REQCD_IMAGE_CROP);
        } catch (Exception e) {
        }
    }

    protected Uri copyCropImageForTravel(long travelId) {
        if (mCurrentPhotoUri == null) return null;
        final File srcFile = new File(mCurrentPhotoUri.getPath());
        mCurrentPhotoUri = null;
        if (!srcFile.exists()) {
            Log.d(TAG, "copyCropImageForTravel: Not Exists : " + srcFile.getAbsolutePath());
            return null;
        }
        final File rootDir = new File(getFilesDir(), "t" + travelId);
        if (!rootDir.exists()) rootDir.mkdirs();
        final File targetFile = new File(rootDir, srcFile.getName());
        final File thumbFile = new File(rootDir, MyConst.THUMBNAIL_PREFIX + srcFile.getName());
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        FileOutputStream thumbFos = null;
        try {
            //copy File
            sourceChannel = new FileInputStream(srcFile).getChannel();
            destChannel = new FileOutputStream(targetFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0 , sourceChannel.size());

            //make thumbnail image
            thumbFos = new FileOutputStream(thumbFile);
            Bitmap imageBitmap = BitmapFactory.decodeFile(srcFile.getAbsolutePath());
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, MyConst.THUMBNAIL_SIZE,
                    MyConst.THUMBNAIL_SIZE, false);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, thumbFos);
            thumbFos.flush();
            mCurrentPhotoUri = Uri.fromFile(targetFile);
            return Uri.fromFile(thumbFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sourceChannel != null) sourceChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (destChannel != null) destChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (thumbFos != null) thumbFos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_IMAGE_CAMERA: {
                if (mCurrentPhotoUri == null) return;
                cropImage(mCurrentPhotoUri);
            }
            break;
            case MyConst.REQCD_IMAGE_CROP: {
                Log.d(TAG, "onActivityResult: mCurrentPhotoUri = " + mCurrentPhotoUri);
            }
            break;
            case MyConst.REQCD_IMAGE_GALLERY: {
                mCurrentPhotoUri = null;
                if (data.getData() == null) return;
                try {
                    Uri uri = data.getData();
                    if (uri == null) return;
                    cropImage(uri);
                } catch (Exception e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Failed to load image.",
                            Snackbar.LENGTH_LONG).show();
                }
            }
            break;
        }
    }
}
