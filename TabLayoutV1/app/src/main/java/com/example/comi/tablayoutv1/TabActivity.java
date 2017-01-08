package com.example.comi.tablayoutv1;


import android.content.Context;
import android.content.Intent;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class TabActivity extends AppCompatActivity {
    static int currentitem=1;
    static TextView Timevalue,Timesvalue;
    ViewPager viewPager;

    ImageButton buttonPlay ;
    ImageButton buttonPause ;
    ImageButton buttonStop ;


    static int[][][] judgment;
    public static int[] tTimesvalue = {0};

    static boolean play;
    static int Notivalue = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Timevalue=(TextView)findViewById(R.id.TextView_tTimevalue);
        Timesvalue=(TextView)findViewById(R.id.TextView_Timesvalue);

        //Fragment+ViewPager+FragmentViewPager组合的使用
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        //Tab 數量
        int TabCount = adapter.getCount();
        tTimesvalue = new int[TabCount+1];

        //TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentitem = (tab.getPosition() + 1);
                Timesvalue.setText(String.valueOf(tTimesvalue[currentitem]));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //toolbar return
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ReturnDialog();
             }
         });

        NumberPicker numPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numPicker.setMaxValue(50);
        numPicker.setMinValue(0);
        numPicker.setValue(1);
        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Notivalue = newVal;
            }
        });

        buttonPlay = (ImageButton)findViewById(R.id.button_play);
        buttonPause = (ImageButton)findViewById(R.id.button_pause);
        buttonStop = (ImageButton)findViewById(R.id.button_stop);

        play = false;
        ButtonListener();

        judgment= new int[TabCount][8][5];

        readvalue();

        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        wl.acquire();

    }

    public void readvalue() {
        int endIdx,endlineIdx,bytes;
        String fullMessage="";
        int judgcount=0;
        int actions=0;
        try {
            FileInputStream fis = openFileInput("value.txt");

            /*if(fis.available()>0)
                Toast.makeText(getApplicationContext(),"fis open",Toast.LENGTH_SHORT).show();*/


            BufferedInputStream bfr=new BufferedInputStream(fis);
            StringBuilder str=new StringBuilder();
            byte[] bfrbyte =new byte[1];

            String endflag="#";
            String endlineflag="@";
            while(-1!=(bytes=bfr.read(bfrbyte))){
                str.append(new String(bfrbyte,0,bytes));
                endIdx=str.indexOf(endflag);
                endlineIdx=str.indexOf(endlineflag);
                if(endlineIdx!=-1){
                    actions++;
                    judgcount=0;
                    str.delete(0, endlineIdx + endlineflag.length());


                }

                if(endIdx!=-1){
                    fullMessage = str.substring(0, endIdx + endflag.length()-1);
                    str.delete(0, endIdx + endflag.length());

                    splitojudgmnet(actions,judgcount,fullMessage);
                    judgcount++;

                }


            }

            bfr.close();

        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(),"fis null", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void splitojudgmnet(int act,int judgcnt,String fullmsg){
        String[] strsplit=fullmsg.split(",");
        if(strsplit.length>0){
            for(int i=0;i<strsplit.length;i++)
                judgment[act][judgcnt][i]=Integer.parseInt(strsplit[i]);

            //Toast.makeText(this,Integer.toString(judgment[1][judgcnt][3]),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        currentitem = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                //TestMode();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        ReturnDialog();
    }

    private void ReturnDialog(){

         /*if(toast == null){
                toast = Toast.makeText(TabActivity.this, "Return", Toast.LENGTH_SHORT);
            }
            else {
                toast.setText("Return");
            }
            toast.show();*/


        new AlertDialog.Builder(TabActivity.this)
                .setTitle("Return Message")
                .setMessage("Do you want to stop?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(TabActivity.this, EndActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                /*        //button at the left of dialog
                .setNeutralButton("return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "return previous page", Toast.LENGTH_SHORT).show();

                        finish();

                    }
                })*/
                .show();
    }

    public static boolean changeview(){
        if(play)
        tTimesvalue[currentitem]++;

        Timesvalue.setText(String.valueOf(tTimesvalue[currentitem]));

        if((tTimesvalue[currentitem]%Notivalue)==0) return true;

        return false;
    }
    public static void changetime(){
        if(play)
        tTimesvalue[0]++;

        if(tTimesvalue[0] >= 3600) {
            Timevalue.setTextScaleX(0.9f);
            Timevalue.setText(String.valueOf(tTimesvalue[0] / 3600) + " : " + String.format("%02d", tTimesvalue[0] / 60 % 60) + " : " + String.format("%02d", tTimesvalue[0] % 60));
        }
        else {
            Timevalue.setText("  " + Integer.toString(tTimesvalue[0] / 60) + " : " + String.format("%02d", tTimesvalue[0] % 60));
        }
    }

    private void ButtonListener(){

        class buttonListener implements View.OnClickListener, View.OnTouchListener {

            public void onClick(View v) {
                if(v.getId() == R.id.button_play){
                    play = true;
                }
                else if(v.getId() == R.id.button_pause){
                    play = false;
                }
                else if(v.getId() == R.id.button_stop){
                    play = false;
                    ReturnDialog();
                }
            }

            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.button_play){
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        buttonPlay.setBackgroundResource(R.drawable.play);
                    }
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonPlay.setBackgroundResource(R.drawable.play_down);
                    }
                }
                else if(v.getId() == R.id.button_pause){
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        buttonPause.setBackgroundResource(R.drawable.pause);
                    }
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonPause.setBackgroundResource(R.drawable.pause_down);
                    }
                }
                else if(v.getId() == R.id.button_stop){
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        buttonStop.setBackgroundResource(R.drawable.stop);
                    }
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonStop.setBackgroundResource(R.drawable.stop_down);
                    }
                }
                return false;
            }
        }

        buttonPlay.setOnClickListener(new buttonListener());
        buttonPlay.setOnTouchListener(new buttonListener());
        buttonPause.setOnClickListener(new buttonListener());
        buttonPause.setOnTouchListener(new buttonListener());
        buttonStop.setOnClickListener(new buttonListener());
        buttonStop.setOnTouchListener(new buttonListener());

    }

    public void TestMode(){

        final View item = LayoutInflater.from(TabActivity.this).inflate(R.layout.setting_test, null);
        final EditText editText_test = (EditText) item.findViewById(R.id.editText_test);

        new AlertDialog.Builder(TabActivity.this)
                .setTitle("input item " + currentitem + " times")
                .setView(item)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int InputTimes = 0;
                        if (editText_test.length() > 0) {
                            InputTimes = Integer.parseInt(editText_test.getText().toString());
                        }

                        if (play) {

                            while (tTimesvalue[currentitem] < InputTimes) {
                                changeview();
                            }
                        }

                    }
                })
                .show();
    }

}

