package com.libra.app.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.libra.app.R;
import com.libra.app.databinding.ActivityHistoryBinding;
import com.libra.app.max_table.Block;
import com.libra.app.max_table.Board;
import com.libra.app.max_table.Table;
import com.libra.app.model.Manifest;
import com.libra.app.utilities.ToastUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.realm.Realm;

public class HistoryActivity extends AppCompatActivity {

    private static Manifest manifest;
    private ActivityHistoryBinding binding;
    private Context context;
    private Realm realm;

    private static final String TAG = "Histy";

    private double profit;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread thread;

    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private int size ;
    private final static int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        context = HistoryActivity.this;
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lagos --> Asaba(Onitsha)");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.setManifest(manifest);
        compute();
        init();
    }

    public static void start(Context c, Manifest mani) {
        c.startActivity(new Intent(c, HistoryActivity.class));
        manifest = mani;
    }

    private void compute(){
        profit = manifest.amt - manifest.dispatch - manifest.miscellaneous - manifest.amt_loading;
        binding.setProfit(String.valueOf(profit));
    }

    private void init(){
        String company = ""
                +"Libra Motors\n"
                +"01/11 Ago okota rd\n"
                +"oshodi-isolo LGA\n"
                +"phone: 0802 000 0000, 0702 000 0000"
                +"\n"
                +"Manifest File"
                +" ";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        List<String> t1Headers = Arrays.asList("INFO", "CUSTOMER");
        List<List<String>> t1Rows = Arrays.asList(
                Arrays.asList("DATE: " + manifest.date + "", "" + "Osas"),
                Arrays.asList("TIME: " + sdf.format(calendar.getTime()) + "", "Manifest No:" + 1 + "")
        );
        String t2Desc = "SELLING DETAILS";
        List<String> t2Headers = Arrays.asList("ENTITY", "Total");

        List<List<String>> rowsList = Arrays.asList(
                Arrays.asList("Passengers", String.valueOf(manifest.passengers)),
                Arrays.asList("Payment", String.valueOf(manifest.amt)),
                Arrays.asList("Dispatch", String.valueOf(manifest.dispatch)),
                Arrays.asList("Transload", String.valueOf(manifest.transload)),
                Arrays.asList("Miscellaneous", String.valueOf(manifest.miscellaneous)),
                Arrays.asList("Loading", String.valueOf(manifest.loading)),
                Arrays.asList("Loading amount", String.valueOf(manifest.amt_loading))
        );

        List<Integer> t2ColWidths = Arrays.asList(16, 9);
        String summary = ""
                + "Sub Total\n";

        String comment = manifest.comment == null ? "No Comment" : manifest.comment;

        String summaryVal = ""
                + String.valueOf(profit) + "\n";
        String sign1 = ""
                +"------Comment Section------\n"
                + comment;
        String advertise = "******** Soulution by Maxwell *******\n* maxsteelobinna@gmail.com*";
        try {
            Board b = new Board(48);
            b.setInitialBlock(new Block(b, 40, 6, company).allowGrid(true).setBlockAlign(Block.BLOCK_LEFT).setDataAlign(Block.DATA_CENTER));
            b.appendTableTo(0, Board.APPEND_BELOW, new Table(b, 42, t1Headers, t1Rows));
            b.getBlock(3).setBelowBlock(new Block(b, 40, 1, t2Desc).setDataAlign(Block.DATA_CENTER));
            b.appendTableTo(5, Board.APPEND_BELOW, new Table(b, 40, t2Headers, rowsList, t2ColWidths));
            Block summaryBlock = new Block(b, 30, 1, summary).setDataAlign(Block.DATA_TOP_RIGHT);
            b.getBlock(10).setBelowBlock(summaryBlock);
            Block summaryValBlock = new Block(b, 9, 1, summaryVal).setDataAlign(Block.DATA_TOP_RIGHT);
            summaryBlock.setRightBlock(summaryValBlock);
            Block sign1Block = new Block(b, 40, 1, sign1).setDataAlign(Block.DATA_CENTER);
            b.getBlock(14).setBelowBlock(sign1Block);
            Block sign2Block = new Block(b, 40, 2, advertise).setDataAlign(Block.DATA_CENTER);
            b.getBlock(16).setBelowBlock(sign2Block);
            System.out.println(b.invalidate().build().getPreview());
            Log.d(TAG, "init: " + b.invalidate().build().getPreview());
        }catch (Exception e){
            ToastUtil.makeLongToast(context, e.getMessage());
        }

    }

    private void beginListenData()
    {
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.makeLongToast(context, "printing");
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }

                            }
                        }catch(Exception ex){
                            stopWorker=true;
                            Log.d(TAG, "run: " + ex.getMessage());
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "beginListenData: " + ex.getMessage());
        }

    }

    private void openBluetoothPrinter() throws IOException
    {
        //standard uuid from string//
        bluetoothAdapter.cancelDiscovery();
        try
        {

            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();
            ToastUtil.makeLongToast(context, "Connected to "+bluetoothDevice.getName()+"");


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(TAG, "openBluetoothPrinter: " + ex.getMessage());
        }
    }

    private void FindBluetoothDevice()
    {
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                ToastUtil.makeLongToast(context, "Can't find bluetooth printer");
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
                openBluetoothPrinter();
            }
            else if (!bluetoothAdapter.isEnabled())
            {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                disconnectBT();
            }

            bluetoothAdapter.startDiscovery();

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            /*if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){

                    if(pairedDev.getName().equals("SP200")){
                        bluetoothDevice=pairedDev;
                        ToastUtil.makeLongToast(context, "Check that your printer is not connected to another device");
                        break;
                    }
                    else if (pairedDev.getAddress().equalsIgnoreCase("0f:02:17:a2:6f:99")){
                        bluetoothDevice = pairedDev;
                        ToastUtil.makeLongToast(context, "Check that your printer is not connected to another device");
                        disconnectBT();
                    }
                }
            }*/
        }catch(Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "FindBluetoothDevice: " + ex.getMessage());
        }
    }

    private void disconnectBT() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "disconnectBT: " + ex.getMessage());
        }

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "onReceive: " + deviceName);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
    }
}
