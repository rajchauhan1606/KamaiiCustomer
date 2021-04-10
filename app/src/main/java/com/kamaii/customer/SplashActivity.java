package com.kamaii.customer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.AppIntro;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.ProjectUtils;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;
    MediaPlayer mediaPlayer;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc,readstorage,wakelock,call;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE ,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK,Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        prefference = SharedPrefrence.getInstance(SplashActivity.this);
        check();
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = null;

            if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
                Intent in = new Intent(SplashActivity.this, BaseActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }else {
                Intent in = new Intent(SplashActivity.this, AppIntro.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23)
        {
            boolean isCamera=false,isReadPhoneState=false,isExternalStrage = false, isFinelocation = false,isReadStorage=false,iscrose=false;

            try {
                int hasFinelocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasFinelocationPermission != PackageManager.PERMISSION_GRANTED) {
                    isFinelocation = false;
                } else {
                    isFinelocation = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isFinelocation = true;
            }

            try {
                int hasReadStoragePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    iscrose = false;
                } else {
                    iscrose = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                iscrose = true;
            }

            if (!isFinelocation || !iscrose) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 007);
            }
            if (isFinelocation && iscrose) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    public boolean check()
    {
        if(checkPermission())
        {
            handler.postDelayed(mTask, SPLASH_TIME_OUT);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                    fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);


                    readstorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.READ_STORAGE, readstorage);


                    wakelock = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.WACK_LOCK, wakelock);

                    call = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CALL_PHONE, call);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        check();
    }

}


