package com.troy.xifan.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;
import com.troy.xifan.R;

/**
 * Created by chenlongfei on 2016/12/2.
 */

public final class UIUtils {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View view, String msg) {
        Snackbar.make(view, "snackbar", Snackbar.LENGTH_LONG).show();
    }

    public static void showDialog(Context context, String msg, final OnDialogListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton(context.getString(R.string.text_dialog_negative),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onCancel();
                    }
                });
        builder.setPositiveButton(context.getString(R.string.text_dialog_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        callback.onConfirm();
                    }
                });
        builder.create().show();
    }

    public static float dip2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static float px2dip(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value,
                context.getResources().getDisplayMetrics());
    }

    public static float getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static float getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public interface OnDialogListener {
        void onConfirm();

        void onCancel();
    }
}
