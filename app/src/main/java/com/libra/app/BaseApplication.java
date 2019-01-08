package com.libra.app;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application{

        @Override
        public void onCreate() {
            super.onCreate();

            Realm.init(this);
            RealmConfiguration configuration = new RealmConfiguration.Builder()
                    .schemaVersion(1)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(configuration);

            AndroidNetworking.initialize(getApplicationContext());
        }
}
