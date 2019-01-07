package kr.changhan.mytravels.base;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

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
}
