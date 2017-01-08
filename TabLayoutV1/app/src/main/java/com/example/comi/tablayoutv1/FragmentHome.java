package com.example.comi.tablayoutv1;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by MkHo on 2015/10/13.
 */
public class FragmentHome extends Fragment {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    String mConnectedDeviceName = null;
    public Button button = null;
    public TextView TextView_BTstate = null;
    Timer timer;

    static int[] values;
    private judgmentthread mjudthread=null;

    LinearLayout layout_home;
    TextView PassTime;
    TextView[] WorkTimes = new TextView[8];
    int[] TotalData = new int[8];

    PagerAdapter adapter = new PagerAdapter(getFragmentManager(), getContext());
    View v;

    boolean DataNull = false;
    TextView HowToUse;

    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String nameField = "NAME";

    TextView hello;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        layout_home = (LinearLayout)v.findViewById(R.id.layout_home);
        button = (Button) v.findViewById(R.id.button_start);
        TextView_BTstate = (TextView)v.findViewById(R.id.textView_BTstate);
        hello = (TextView)v.findViewById(R.id.TextView_hello);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();




        if(mjudthread==null){
            mjudthread=new judgmentthread(mHandler);
            mjudthread.start();
        }


        if(timer == null)
        timer =new Timer();
        try {
            timer.schedule(timerTask, 0, 1000);
        }catch (Exception e) {
            e.printStackTrace();
        }


        WriteValue();


        settings = getActivity().getSharedPreferences(data,0);
        hello.setText("Hello! "+settings.getString(nameField, ""));


        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            button.setText("Enable");
            TextView_BTstate.setText("Bluetooth is not in work.");
        }
        else if(BluetoothChatService.mState != BluetoothChatService.STATE_CONNECTED){
            button.setText("CONNECT");
            TextView_BTstate.setText("Waiting for a connect.");
        }
        else {
            button.setText("START");
            if(mChatService==null)
            mChatService = new BluetoothChatService(getActivity(),mHandler);
        }

        button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    // Otherwise, setup the chat session
                } else if (BluetoothChatService.mState == BluetoothChatService.STATE_CONNECTED) {
                    startActivity(new Intent(getActivity(), TabActivity.class));
                } else {
                    mChatService = new BluetoothChatService(getActivity(), mHandler);
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

                }


            }
        });

        for(int i = 0 ; i<TotalData.length;i++)
            TotalData[i] = 0;

        ReadDataValue();

        if(DataNull) {
            HowToUse();
        }
        else{
            File f=new File(getActivity().getFilesDir(),"data.txt");
            if(f.exists())
                ShowData();
        }


    }

    private void HowToUse(){
        layout_home.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.gravity = Gravity.CENTER;

        HowToUse = new TextView(getActivity());
        HowToUse.setTextSize(20f);
        HowToUse.setGravity(Gravity.CENTER);
        HowToUse.setTextColor(Color.GREEN);

        HowToUse.setText("\n\n- 歡迎使用運動暨復建手環APP -\n\n\n" +
                "1. 請確認您的手環已經穿戴正確。\n\n" +
                "2. 側邊選單可記錄您的個人資訊。\n\n" +
                "3. 請確認您的藍芽已開啟並配對。");

        ImageView logo = new ImageView(getActivity());

        logo.setBackgroundResource(R.drawable.rcs_png);
        logo.setLayoutParams(params);


        layout_home.addView(HowToUse);
        layout_home.addView(logo);

    }

    private void ShowData(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(150, 0, 0, 0);

        layout_home.removeAllViews();

        PassTime  = new TextView(getActivity());
        PassTime.setTextSize(24f);
        PassTime.setGravity(2);
        PassTime.setLayoutParams(params);
        PassTime.setTextColor(Color.WHITE);
        if(TotalData[0] >= 3600) {
            //PassTime.setTextScaleX(0.9f);
            PassTime.setText("\nTotal Time:  " + String.valueOf(TotalData[0] / 3600) + " : " + String.format("%02d", TotalData[0] / 60 % 60) + " : " + String.format("%02d", TotalData[0] % 60) + "\n");
        }
        else {
            PassTime.setText("\nTotal Time:  " + String.valueOf(TotalData[0] / 60) + " : " + String.format("%02d", TotalData[0] % 60) + "\n");
        }
        layout_home.addView(PassTime);

        for(int i = 1; i < 8; i++) {

            if(TotalData[i] > 0 ){
                WorkTimes[i] = new TextView(getActivity());
                WorkTimes[i].setTextSize(18f);
                WorkTimes[i].setGravity(2);
                WorkTimes[i].setLayoutParams(params);
                WorkTimes[i].setTextColor(Color.WHITE);
                WorkTimes[i].setText(adapter.getPageTitle(i - 1) + ":  " + TotalData[i] + "\n");
                layout_home.addView(WorkTimes[i]);
            }
        }
    }

    public void WriteValue(){
        File f=new File(getActivity().getFilesDir(),"value.txt");
        if (!f.exists()) {//////!!!
            try {
                FileOutputStream fos = getActivity().openFileOutput("value.txt", Context.MODE_PRIVATE);
                //BufferedOutputStream bfr=new BufferedOutputStream(fos);

                //fos.write(id.getText().toString().getBytes());
                fos.write(("0,0,0,6100,5400#0,0,0,5400,5700#0,0,0,4400,5400#0,0,0,4100,5300#0,0,0,3900,5300#0,0,0,4100,5300#0,0,0,4400,5400#0,0,0,6100,5400#@" +
                        "0,0,0,4600,5900#0,0,0,5000,6000#0,0,0,5500,6000#0,0,0,6100,4700#0,0,0,6000,4400#0,0,0,6100,4700#0,0,0,5500,6000#0,0,0,4600,5900#@" +
                        "0,0,0,6200,4900#0,0,0,6000,4800#0,0,0,5400,4800#0,0,0,5000,4700#0,0,0,5600,4800#0,0,0,5800,4800#0,0,0,6100,4900#0,0,0,6200,4900#@" +
                        "0,0,0,6200,5100#0,0,0,6100,5000#0,0,0,5600,5000#0,0,0,5000,4800#0,0,0,5600,4900#0,0,0,5900,5000#0,0,0,6200,5000#0,0,0,6200,5100#@" +
                        "0,0,0,6200,5000#0,0,0,6000,4800#0,0,0,5500,4600#0,0,0,5000,4500#0,0,0,5600,4700#0,0,0,5600,4700#0,0,0,6200,4900#0,0,0,6200,5000#@").getBytes());
                //fos.write(password.getText().toString().getBytes());
                //fos.write("\n".getBytes());
                fos.close();
                //Toast.makeText(getContext(), "save", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), getActivity().getFileStreamPath("test.txt").toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//else Toast.makeText(getContext(),"value.txt exist", Toast.LENGTH_SHORT).show();

    }

    public void ReadDataValue() {
        int endIdx,bytes;
        String fullMessage="";

        try {
            FileInputStream fis = getActivity().openFileInput("data.txt");

            //if(fis.available()>0)
                //Toast.makeText(getActivity(),"data open",Toast.LENGTH_SHORT).show();

            BufferedInputStream bfr=new BufferedInputStream(fis);
            StringBuilder str=new StringBuilder();
            byte[] bfrbyte =new byte[1];

            String endflag="#";

            while(-1!=(bytes=bfr.read(bfrbyte))){
                str.append(new String(bfrbyte,0,bytes));
                endIdx=str.indexOf(endflag);

                if(endIdx!=-1){
                    fullMessage = str.substring(0, endIdx + endflag.length()-1);
                    str.delete(0, endIdx + endflag.length());
                    Split_toData(fullMessage);
                }
            }
            DataNull = false;
            bfr.close();
        } catch (IOException e) {
            DataNull = true;
            //Toast.makeText(getActivity(),"data null", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void Split_toData(String fullmsg){
        String[] strsplit=fullmsg.split(",");

        if(strsplit.length>0){
            for(int i=0;i<strsplit.length;i++)
                TotalData[i]+=Integer.parseInt(strsplit[i]);

            // Toast.makeText(getActivity(),Integer.toString(strsplit.length),Toast.LENGTH_SHORT).show();
        }
    }


    private TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 7;
            mHandler.sendMessage(msg);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    mChatService = new BluetoothChatService(getActivity(),mHandler);
                    connectDevice(data, true);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

                    button.setText("CONNECT");
                    TextView_BTstate.setText("Waiting for a connect...");

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d("BlueToothChat", "BT not enabled");
                    Toast.makeText(getActivity(), "Bluetooth not enabled",Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        //String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        //Toast.makeText(getActivity(), device.toString()+secure,Toast.LENGTH_SHORT).show();

        mChatService.connect(device, secure);
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus("Connect to " + mConnectedDeviceName );
                            button.setText("START");
                            startActivity(new Intent(getActivity(), TabActivity.class));
                           // mConversationArrayAdapter.clear();
                            //mThread.start();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus("Connecting.");
                            button.setText("CONNECT");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            setStatus("Waiting for a connect.");
                            break;
                        case BluetoothChatService.STATE_NONE:
                            setStatus("Not Connect.");
                            button.setText("CONNECT");
                            break;
                    }
                    break;
                /*case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;*/
                case Constants.MESSAGE_READ:
                    //byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    String readMessage = (String) msg.obj;

                   // mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);

                    String[] strsplit=readMessage.split(",");
                    values=new int[strsplit.length];
                    for (int i=0;i<strsplit.length;i++){
                        values[i]=Integer.parseInt(strsplit[i]);
                    }




                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 7:
                    if (TabActivity.Timevalue!=null) TabActivity.changetime();
                    break;
                case 8:
                   /* String test=(String)msg.obj;
                    Toast.makeText(activity, test,Toast.LENGTH_SHORT).show();*/
                    if(TabActivity.changeview() && TabActivity.play) mNotify();
                    break;

            }
        }
    };

    public void mNotify(){
        Notification.Builder noBuilder = new Notification.Builder(getActivity());
        noBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager noManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        noManager.notify(0, noBuilder.build());

    }

    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }

        if(subTitle == "Connecting.")
            button.setClickable(false);
        else
            button.setClickable(true);

        TextView_BTstate.setText(subTitle);
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        timer.cancel();
        mjudthread.stop();
        if (mChatService != null) {
            mChatService.stop();
            timerTask.cancel();

        }
    }

}
