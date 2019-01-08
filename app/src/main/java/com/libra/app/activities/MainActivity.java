package com.libra.app.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.libra.app.R;
import com.libra.app.databinding.ActivityMainBinding;
import com.libra.app.model.User;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Context context;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = MainActivity.this;
        realm = Realm.getDefaultInstance();
        loadUi();
    }

    private void loadUi(){
        User data = realm.where(User.class).findFirst();
        if (data == null){
            goToLogin(context);
        }else {
            goToHome(context);
        }
    }

    private void goToLogin(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToHome(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
