package com.libra.app.utilities;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void makeLongToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
