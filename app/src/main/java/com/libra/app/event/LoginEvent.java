package com.libra.app.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.libra.app.activities.HomeActivity;
import com.libra.app.databinding.ActivityLoginBinding;
import com.libra.app.model.User;
import com.libra.app.utilities.Ui;

import io.realm.Realm;


public class LoginEvent {

    private ActivityLoginBinding binding;
    private static final String TAG = LoginEvent.class.getSimpleName();
    private Context context;
    private Activity activity;

    public LoginEvent(ActivityLoginBinding binding, Context context, Activity activity) {
        this.binding = binding;
        this.context = context;
        this.activity = activity;
    }

    public void onClick(final View view){
        //Reset errors
        binding.emailAddress.setError(null);
        binding.password.setError(null);

        String email = binding.emailAddress.getText().toString();
        String password = binding.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)){
            binding.password.setError("Please enter a password");
            focusView = binding.password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            binding.emailAddress.setError("Please enter your username");
            focusView = binding.emailAddress;
            cancel = true;
        } else if (!isEmailValid(email)) {
            binding.emailAddress.setError("Please enter your username");
            focusView = binding.emailAddress;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            Log.d(TAG, "onClickLogin: nopeee");
        } else {

            connectToServer(view);
            Log.d(TAG, "onClickLogin: connecting");
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    private void connectToServer(View view){
        final String username = binding.emailAddress.getText().toString();
        final String password = binding.password.getText().toString();

        Ui.showNonCloseableProgress(context, "Connecting to server");
        Ui.forceHideKeyboard(context, view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (password.equalsIgnoreCase("password")){

                    Realm realm = null;
                    try {
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                Ui.dismissProgress();
                                User user = realm.createObject(User.class);
                                user.username = username;
                                goToHome(context);
                            }
                        });
                    }catch (Exception e){
                        Ui.dismissProgress();
                        Ui.showErrorDialog(context, "Login Error", e.getMessage());
                    }finally {
                        if (realm != null){
                            realm.close();
                        }
                    }
                }else {
                    Ui.dismissProgress();
                    Ui.showErrorDialog(context, "Error", "Incorrect password");
                }
            }
        }, 1000);
    }

    private void goToHome(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        activity.finish();
    }
}
