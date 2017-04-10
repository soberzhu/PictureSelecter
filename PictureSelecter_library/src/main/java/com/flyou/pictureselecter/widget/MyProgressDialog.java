package com.flyou.pictureselecter.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyou.pictureselecter.R;


/**
 * Created by apple on 15/5/19.
 */
public class MyProgressDialog {
    private Dialog dialog = null;

    public void show(@NonNull Context context, String msg, boolean cancelOnTouchOutside, boolean cancelable, DialogInterface.OnCancelListener cancel) {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.dialog);
        }
        View view = LayoutInflater.from(context).inflate(
                R.layout.xdja_picture_selector_dialog_spinner_progress, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.setCancelable(cancelable);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.picture_selector_progress_dialog_height);
        lp.width = context.getResources().getDimensionPixelSize(R.dimen.picture_selector_progress_dialog_width);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        TextView message = (TextView) view.findViewById(R.id.dialog_message);
        if (cancel != null) {
            dialog.setOnCancelListener(cancel);
        }
        if (msg != null) {
            message.setText(msg);
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }

        if (context instanceof Activity){
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
        } else {
            dialog.getWindow()
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }
    }


    public void show(Context context, String msg) {
        show(context, msg, false, true, null);
    }

    public void show(Context context, String msg, DialogInterface.OnCancelListener cancel) {
        show(context, msg, false, true, cancel);
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
