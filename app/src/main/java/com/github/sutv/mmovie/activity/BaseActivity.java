package com.github.sutv.mmovie.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.github.sutv.mmovie.MainApplication;
import com.github.sutv.mmovie.di.ActivityComponent;
import com.github.sutv.mmovie.di.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @NonNull
    public ActivityComponent getComponent() {
        if (activityComponent == null) {
            MainApplication mainApplication = (MainApplication) getApplication();
            activityComponent = mainApplication.getComponent().plus(new ActivityModule(this));
        }
        return activityComponent;
    }
}
