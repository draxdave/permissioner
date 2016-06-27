package ir.drax.permissioner;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ir.drax.permissions.PermissonManager;

public class MainActivity extends AppCompatActivity {

    private String[] manifestPermissions={Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    RelativeLayout permission_form,app_msg;
    Boolean isAppActivated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //just two text place holders..
        permission_form = (RelativeLayout) findViewById(R.id.flContent_permission_form);
        app_msg = (RelativeLayout) findViewById(R.id.dashboard_activation_msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (PermissonManager.hasPermissions(this, manifestPermissions, false)) {
                isAppActivated = true;
                app_msg.setVisibility(View.VISIBLE);

                //permissions has been granted so
                //every thing is OK - do your stuff
                Toast.makeText(MainActivity.this, "Great!\nYour app already granted required permissions!", Toast.LENGTH_SHORT).show();

            } else {
                permission_form.setVisibility(View.VISIBLE);
                isAppActivated = false;
            }
        }
        else {
            permission_form.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "you dont need Permission Manager!\n\nAndroid 6(ver 23) or higher needed.", Toast.LENGTH_LONG).show();
        }
    }


    public void chkPermissionsBtn(View v) {
        if(PermissonManager.hasPermissions(this,manifestPermissions, true)){
            isAppActivated=true;
            app_msg.setVisibility(View.VISIBLE);

            //now permissions granted so
            //every thing is fine - do your stuff
            Toast.makeText(MainActivity.this, "Permissions Granted successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(PermissonManager.onRequestPermissionsResult(this,requestCode,permissions,grantResults)){
            permission_form.setVisibility(View.GONE);
            app_msg.setVisibility(View.VISIBLE);
            //now permissions granted so
            //every thing is fine - do your stuff
            Toast.makeText(MainActivity.this, "Permissions Granted successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(PermissonManager.onActivityResult(this,manifestPermissions,requestCode,resultCode,data)){
            permission_form.setVisibility(View.GONE);
            app_msg.setVisibility(View.VISIBLE);
            //now permissions granted so
            //every thing is fine - do your stuff
            Toast.makeText(MainActivity.this, "Permissions Granted successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
