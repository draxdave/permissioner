package ir.drax.permissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import ir.drax.permissioner.R;


/**
 * Created by Drax on 06/27/2016.
 */
public class PermissonManager {
    static int MY_PERMISSIONS_ID=1;
    static int OVERLAY_PERMISSION_REQ_CODE=1221;
    static int REQUEST_PERMISSION_SETTING=1222;



    @SuppressLint("NewApi")
    private static ArrayList<String[]> getDenied(String[] reqPermissions, Activity activity){
        ArrayList<String[]> deniedPermissions = new ArrayList<String[]>();
        for (String reqPermission : reqPermissions) {
            int e = reqPermission.indexOf("SYSTEM_ALERT_WINDOW");//check to see if there is super security permission request to handle it in a right way
            if (e >= 0) {
                if (!Settings.canDrawOverlays(activity)) {//check SYSTEM_ALERT_WINDOW permission
                    deniedPermissions.add(new String[]{reqPermission, "false"});
                }
                continue;

            }
            if (ContextCompat.checkSelfPermission(activity, reqPermission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        reqPermission)) {
                    deniedPermissions.add(new String[]{reqPermission, "true"});
                } else {
                    deniedPermissions.add(new String[]{reqPermission, "false"});
                }
            }
        }
        return deniedPermissions;
    }

    public static Boolean hasPermissions(Activity activity, String[] reqPermissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String[]> deniedPermissions = getDenied(reqPermissions, activity);
            if (deniedPermissions.size() > 0) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static Boolean hasPermissions(Activity activity, String[] reqPermissions,Boolean askForPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String[]> deniedPermissions = getDenied(reqPermissions, activity);
            if (deniedPermissions.size() > 0) {
                if(askForPermission){
                    String[] denied_permissions= new String[deniedPermissions.size()];
                    for(int i=0;i<deniedPermissions.size();i++)
                        denied_permissions[i]=deniedPermissions.get(i)[0];
                    grantPermissionsIfNeeded(activity,denied_permissions);
                }
                return false;
            }
            return true;
        }
        return true;
    }

    private static void grantPermissionsIfNeeded(Activity activity, String[] reqPermissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (String perm : reqPermissions) {
                int i = perm.indexOf("SYSTEM_ALERT_WINDOW");
                if (i >= 0) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                    return;

                }
            }

            ActivityCompat.requestPermissions(activity, reqPermissions, MY_PERMISSIONS_ID);
        }
    }


     public static Boolean onActivityResult(final Activity activity,String[] reqPermissions, int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    new AlertDialog.Builder(activity).setTitle(R.string.access_warning_first_title).setMessage(R.string.access_warning_first)
                            .setNegativeButton(
                                    R.string.access_warning_exitapp, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }).setPositiveButton(R.string.access_warning_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + activity.getPackageName()));
                            activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                        }
                    }).show();
                    return false;
                }
                else {
                    return hasPermissions(activity,reqPermissions,true);
                }
            }
        }
         else if(requestCode==REQUEST_PERMISSION_SETTING){
            return hasPermissions(activity,reqPermissions,true);
        }
         return true;
    }


    @SuppressLint("NewApi")
    public static Boolean onRequestPermissionsResult(final Activity activity, int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        Boolean permissionsGranted=true;
        for(int i=0;i<grantResults.length;i++) {

            int grantResult=grantResults[i];
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted=false;

                if (!activity.shouldShowRequestPermissionRationale(permissions[i])) {
                    new AlertDialog.Builder(activity).setTitle(R.string.access_warning_second_title
                    ).setMessage(R.string.access_warning_second)
                            .setNegativeButton(R.string.access_warning_exitapp, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }).setPositiveButton(R.string.access_warning_second_gotosettings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        }
                    }).setCancelable(false).show();
                    break;
                }


                new AlertDialog.Builder(activity).setTitle(R.string.access_warning_first_title).setMessage(R.string.access_warning_third)
                        .setNegativeButton(R.string.access_warning_exitapp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        }).setPositiveButton(R.string.access_warning_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        hasPermissions(activity,permissions,true);
                    }
                }).setCancelable(false).show();

                break;
            }
        }

        return permissionsGranted;
    }
}
