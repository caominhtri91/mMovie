package com.github.sutv.mmovie;

import android.app.Application;
import android.support.annotation.NonNull;

//import com.crashlytics.android.Crashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

//import io.fabric.sdk.android.Fabric;
import io.github.droidkaigi.StethoWrapper;
import com.github.sutv.mmovie.di.AppComponent;
import com.github.sutv.mmovie.di.AppModule;
import com.github.sutv.mmovie.di.DaggerAppComponent;
import com.github.sutv.mmovie.util.LocaleUtil;

public class MainApplication extends Application {

    AppComponent appComponent;

    @NonNull
    public AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

//        Fabric.with(this, new Crashlytics());

        new StethoWrapper(this).setup();

        AndroidThreeTen.init(this);

        LocaleUtil.initLocale(this);
    }

}
