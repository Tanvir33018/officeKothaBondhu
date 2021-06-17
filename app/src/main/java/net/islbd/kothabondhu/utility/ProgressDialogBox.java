package net.islbd.kothabondhu.utility;

import android.app.ProgressDialog;
import android.content.Context;

import net.islbd.kothabondhu.R;

public class ProgressDialogBox {
    private ProgressDialog mDialog;

    public ProgressDialogBox(Context mContext) {
        this.mDialog = new ProgressDialog(mContext, R.style.CustomDialogTheme);
        mDialog.setIcon(R.mipmap.ic_launcher);
        mDialog.setTitle("Kotha Bondhu");
        mDialog.setMessage("Processing...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

    }

    public void showDialog(){
        mDialog.show();
    }

    public void dismissDialog(){
        mDialog.dismiss();
    }
}
