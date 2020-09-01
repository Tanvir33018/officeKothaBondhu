package net.islbd.kothabondhu.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

public final class PermissionChecker {

    private final int REQUEST_MULTIPLE_PERMISSION = 100;
    private VerifyPermissionsCallback callbackMultiple;

    private static final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO};

    public void verifyPermissions(Activity activity, VerifyPermissionsCallback callback) {
        String[] denyPermissions = getDenyPermissions(activity, permissions);
        if (denyPermissions.length > 0) {
            ActivityCompat.requestPermissions(activity, denyPermissions, REQUEST_MULTIPLE_PERMISSION);
            this.callbackMultiple = callback;
        } else {
            if (callback != null) {
                callback.onPermissionAllGranted();
            }
        }
    }

    private String[] getDenyPermissions(@NonNull Context context, @NonNull String[] permissions) {
        ArrayList<String> denyPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(permission);
            }
        }
        return denyPermissions.toArray(new String[0]);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && callbackMultiple != null) {
                    ArrayList<String> denyPermissions = new ArrayList<>();
                    int i = 0;
                    for (String permission : permissions) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            denyPermissions.add(permission);
                        }
                        i++;
                    }
                    if (denyPermissions.size() == 0) {
                        callbackMultiple.onPermissionAllGranted();
                    } else {
                        callbackMultiple.onPermissionDeny(denyPermissions.toArray(new String[0]));
                    }
                }
                break;
        }
    }

    public static boolean hasPermissions(@NonNull Context context) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface VerifyPermissionsCallback {
        void onPermissionAllGranted();

        void onPermissionDeny(String[] permissions);
    }
}
