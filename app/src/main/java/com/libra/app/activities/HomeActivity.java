package com.libra.app.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.libra.app.R;
import com.libra.app.databinding.ActivityHomeBinding;
import com.libra.app.model.Manifest;
import com.libra.app.utilities.ToastUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityHomeBinding binding;
    private Context context;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        context = HomeActivity.this;
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lagos --> Asaba(Onitsha)");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.signedUpOpt.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbut = binding.getRoot().findViewById(checkedId);
            if (rbut.getText().toString().equalsIgnoreCase("true")){
                binding.setShow("yes");
            }else if (rbut.getText().toString().equalsIgnoreCase("false")){
                binding.setShow("no");
            }
        });
    }

    public void onClick(View v){
        String passengers = binding.passengers.getText().toString();
        String payment = binding.payment.getText().toString();
        String date = binding.date.getText().toString();
        String dispatch = binding.dispatch.getText().toString();

        String transload = binding.transload.getText().toString();
        String miscell = binding.miscellaneous.getText().toString();

        String loading = binding.loading.getText().toString();
        String load_amt = binding.loadedAmt.getText().toString();
        String comment = binding.comment.getText().toString();

        if (passengers.isEmpty() || payment.isEmpty() || date.isEmpty() || dispatch.isEmpty()){
            ToastUtil.makeLongToast(context, "Please fill above fields");
        }else {
            realm.executeTransaction(realm -> {
                Manifest manifest = realm.createObject(Manifest.class);
                manifest.passengers = Integer.parseInt(passengers);
                manifest.amt = Double.parseDouble(payment);
                manifest.date = date;
                manifest.dispatch = Double.parseDouble(dispatch);
                if (!transload.isEmpty()){
                    manifest.transload = Integer.parseInt(transload);
                }
                if (!miscell.isEmpty()){
                    manifest.miscellaneous = Double.parseDouble(miscell);
                }
                if (!loading.isEmpty()){
                    manifest.loading = Integer.parseInt(loading);
                }
                if (!load_amt.isEmpty()){
                    manifest.amt_loading = Double.parseDouble(load_amt);
                }
                if (!comment.isEmpty()){
                    manifest.comment = comment;
                }

                ToastUtil.makeLongToast(context, "Added Successfully");
                HistoryActivity.start(context, manifest);
            });
        }
    }

    public void onDateClick(View v){
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
        dpd.show(getFragmentManager(), "Tag");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date selected = c.getTime();
        binding.date.setText(formatDate(selected, sqlformat));

    }

    public static SimpleDateFormat dateform = new SimpleDateFormat("dd MMMM yyyy");
    public static SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MM-yyyy");

    public static String formatDate(Date date , SimpleDateFormat dateformat){
        if(dateformat == null){
            dateformat = dateform;
        }
        if(date != null){
            return dateformat.format(date);
        }
        return "";

    }
}
