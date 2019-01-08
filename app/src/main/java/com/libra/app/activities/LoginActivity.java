package com.libra.app.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.libra.app.R;
import com.libra.app.databinding.ActivityLoginBinding;
import com.libra.app.event.LoginEvent;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private LoginEvent loginEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginEvent = new LoginEvent(binding,  this, LoginActivity.this);
        context = LoginActivity.this;
        binding.setHandler(loginEvent);
    }
}
