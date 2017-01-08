package com.example.comi.tablayoutv1;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;


public class EndActivity extends AppCompatActivity {

    int total[] = TabActivity.tTimesvalue;
    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
    LinearLayout layout_end;
    TextView PassTime;
    TextView[] WorkTimes = new TextView[adapter.getCount()];
    Button button_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //display return icon

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnDialog();
            }
        });

        layout_end = (LinearLayout)findViewById(R.id.layout_end);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(150, 0, 0, 0);

        PassTime  = new TextView(this);
        PassTime.setTextSize(28f);
        PassTime.setGravity(2);
        PassTime.setLayoutParams(params);
        PassTime.setTextColor(Color.WHITE);
        if(total[0] >= 3600) {
            PassTime.setText("\nPass Time:  " + String.valueOf(total[0] / 3600) + " : " + String.format("%02d", total[0] / 60 % 60) + " : " + String.format("%02d", total[0] % 60) + "\n");
        }
        else {
            PassTime.setText("\nPass Time:  " + String.valueOf(total[0] / 60) + " : " + String.format("%02d", total[0] % 60) + "\n");
        }
        layout_end.addView(PassTime);


        for(int i = 1; i < adapter.getCount(); i++) {
            if(total[i] > 0){
                WorkTimes[i] = new TextView(this);
                WorkTimes[i].setTextSize(24f);
                WorkTimes[i].setGravity(2);
                WorkTimes[i].setLayoutParams(params);
                WorkTimes[i].setTextColor(Color.WHITE);
                WorkTimes[i].setText(adapter.getPageTitle(i - 1) + ": " + total[i] + "\n");
                layout_end.addView(WorkTimes[i]);
            }
        }

        button_ok = new Button(this);
        button_ok.setText("OK");
        button_ok.setGravity(Gravity.CENTER_HORIZONTAL);
        layout_end.addView(button_ok);

        button_ok.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ReturnDialog();
                WriteDataValue();
                finish();
            }
        });



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
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        ReturnDialog();
    }

    private void ReturnDialog() {

        new AlertDialog.Builder(EndActivity.this)
                .setTitle("Result Message")
                .setMessage("Return to main page?")
                .setPositiveButton("Go!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WriteDataValue();
                        finish();
                    }
                })
                .setNegativeButton("Stay Here", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void WriteDataValue(){

        String Datastr = "";
        for (int i=0;i<total.length;i++){
            Datastr+=Integer.toString(total[i]);
            if (i<total.length)Datastr+=",";
        }
        Datastr+="#";

        try {
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);

            fos.write(Datastr.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "Save success", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), getFileStreamPath("data.txt").toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
