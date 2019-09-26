package com.ksdigtalnomad.koala.ui.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseApplication extends MultiDexApplication {
    private static BaseApplication instance;

    public static BaseApplication getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

//        Typekit.getInstance()
//                .addNormal(Typekit.createFromAsset(this, "fonts/Roboto-Regular.ttf"))
//                .addBold(Typekit.createFromAsset(this, "fonts/Roboto-Medium.ttf"))
//                .addItalic(Typekit.createFromAsset(this, "fonts/Roboto-Italic.ttf"));
//
//        KakaoSDK.init(new KAKAOSDKAdapter());

        MobileAds.initialize(this, (InitializationStatus initializationStatus) -> {});

        printKeyHash();
    }

    public static void printKeyHash() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = instance.getPackageManager().getPackageInfo(instance.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) return;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("Key hash", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return ;
            } catch (NoSuchAlgorithmException e) {
                Log.e("ggg", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
