package com.libra.app.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.libra.app.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * Package com.libra.app.utilities in
 * <p>
 * Project Libra
 * <p>
 * Created by Maxwell on 08/09/2018
 */
public class Ui {

    private static Dialog dialog;

    public static void showOkayDialog(Context context, String title, String content){
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_thumb_up_black_24dp)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.ok, null)
                //.setNegativeButton(android.R.string.no, null)
                .show();
    }

    public static void showErrorDialog(Context context, String title, String content){
        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.ok, null)
                //.setNegativeButton(android.R.string.no, null)
                .show();
    }

    public static void showInfoDialog(Context context, String title, String message){
        new LovelyInfoDialog(context)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_info_outline_black_24dp)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(false)
                .setTitle(title)
                .setMessage(message)
                .show();
    }



    public static void showNonCloseableProgress(Context context, String title){
        dialog =  new LovelyProgressDialog(context)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle(title)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setCancelable(false)
                .show();
    }

    public static void showNonCloseableProgress(Context context){
        dialog =  new LovelyProgressDialog(context)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle(context.getString(R.string.load))
                .setTopColorRes(R.color.colorPrimaryDark)
                .setCancelable(false)
                .show();
    }

    public static void dismissProgress(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void forceHideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    // for single choice
   /* ArrayAdapter<DonationOption> adapter = new DonationAdapter(this, loadDonationOptions());
new LovelyChoiceDialog(this)
      .setTopColorRes(R.color.darkGreen)
      .setTitle(R.string.donate_title)
      .setIcon(R.drawable.ic_local_atm_white_36dp)
      .setMessage(R.string.donate_message)
      .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<DonationOption>() {
        @Override
        public void onItemSelected(int position, DonationOption item) {
            Toast.makeText(context, getString(R.string.you_donated, item.amount),Toast.LENGTH_SHORT).show();
        }
    })
            .show();*/

   // for multi choice

    /*
     * By passing theme as a second argument to constructor - we can tint checkboxes/edittexts.
     * Don't forget to set your theme's parent to Theme.AppCompat.Light.Dialog.Alert
     */
/*
    String[] items = getResources().getStringArray(R.array.food);
new LovelyChoiceDialog(this, R.style.CheckBoxTintTheme)
      .setTopColorRes(R.color.darkRed)
      .setTitle(R.string.order_food_title)
      .setIcon(R.drawable.ic_food_white_36dp)
      .setItemsMultiChoice(items, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
        @Override
        public void onItemsSelected(List<Integer> positions, List<String> items) {
            Toast.makeText(MainActivity.this,
                    getString(R.string.you_ordered, TextUtils.join("\n", items)),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    })
            .setConfirmButtonText(R.string.confirm)
      .show();*/

    // for text input

   /* new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
      .setTopColorRes(R.color.darkDeepOrange)
      .setTitle(R.string.text_input_title)
      .setMessage(R.string.text_input_message)
      .setIcon(R.drawable.ic_assignment_white_36dp)
      .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
        @Override
        public boolean check(String text) {
            return text.matches("\\w+");
        }
    })
            .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
        @Override
        public void onTextInputConfirmed(String text) {
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    })
            .show();*/
}
