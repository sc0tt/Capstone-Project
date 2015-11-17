package io.adie.upscoot;

import android.app.Application;
import android.content.Context;

public class UpscootApplication extends Application {
    private static UpscootApplication instance;

    public static UpscootApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
