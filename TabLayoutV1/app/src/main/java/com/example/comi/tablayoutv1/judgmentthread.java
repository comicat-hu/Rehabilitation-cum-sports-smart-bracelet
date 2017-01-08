package com.example.comi.tablayoutv1;

import android.os.Message;

import android.os.Handler;

import android.util.Log;
import android.widget.Toast;

import java.util.Timer;



        /*Created by MkHo on 2015/10/28.*/


public class judgmentthread {
    private Handler mHandler;

    private int[] values;
    private int currentitem;
    private int[][][] judgment;
    public int count=0;
    private judthread mjudthread =null;
    private timethread timethread =null;
    private boolean judthreadstart=true;
    private boolean timethreadstart=true;
    public judgmentthread(Handler handler) {
        mHandler = handler;
    }

    public synchronized void start() {
        if(mjudthread==null){
            mjudthread = new judthread();
            mjudthread.start();
        }
        if(timethread==null)
        timethread =new timethread();


    }
    public synchronized void stop() {
        if(mjudthread!=null){
            mjudthread.cancel();
            mjudthread=null;
        }
        if (timethread!=null){
            timethread.cancel();
            timethread=null;
        }
    }


    public class judthread extends Thread {

        public void run() {

            while (judthreadstart) {
                values=FragmentHome.values;
                currentitem=TabActivity.currentitem;
                judgment=TabActivity.judgment;
                int tmpcurrent;

                if (values != null&&judgment!=null){
                    switch (currentitem){
                        case 1:
                            while (currentitem==1){
                                if(timethread==null){
                                    timethread=new timethread();
                                    timethread.start();
                                }


                                if(judvalue(currentitem,count)==1)
                                    count++;

                                if(count>7){
                                    Message msg = new Message();
                                    msg.what = 8;
                                    mHandler.sendMessage(msg);
                                    count=0;
                                    timethread.cancel();
                                    timethread=null;
                                }
                                tmpcurrent=currentitem;
                                getnewvalue();
                                if(tmpcurrent!=currentitem){
                                    timethread.cancel();
                                    timethread=null;
                                }


                            }
                            break;
                        case 2:
                            while (currentitem==2){
                                if(timethread==null){
                                    timethread=new timethread();
                                    timethread.start();
                                }


                                if(judvalue(currentitem,count)==1)
                                    count++;

                                if(count>7){
                                    Message msg = new Message();
                                    msg.what = 8;
                                    mHandler.sendMessage(msg);
                                    count=0;
                                    timethread.cancel();
                                    timethread=null;
                                }
                                tmpcurrent=currentitem;
                                getnewvalue();
                                if(tmpcurrent!=currentitem){
                                    timethread.cancel();
                                    timethread=null;
                                }


                            }
                            break;
                        case 3:
                            while (currentitem==3){
                                if(timethread==null){
                                    timethread=new timethread();
                                    timethread.start();
                                }


                                if(judvalue(currentitem,count)==1)
                                    count++;

                                if(count>7){
                                    Message msg = new Message();
                                    msg.what = 8;
                                    mHandler.sendMessage(msg);
                                    count=0;
                                    timethread.cancel();
                                    timethread=null;
                                }
                                tmpcurrent=currentitem;
                                getnewvalue();
                                if(tmpcurrent!=currentitem){
                                    timethread.cancel();
                                    timethread=null;
                                }


                            }
                            break;
                        case 4:
                            while (currentitem==4){
                                if(timethread==null){
                                    timethread=new timethread();
                                    timethread.start();
                                }


                                if(judvalue(currentitem,count)==1)
                                    count++;

                                if(count>7){
                                    Message msg = new Message();
                                    msg.what = 8;
                                    mHandler.sendMessage(msg);
                                    count=0;
                                    timethread.cancel();
                                    timethread=null;
                                }
                                tmpcurrent=currentitem;
                                getnewvalue();
                                if(tmpcurrent!=currentitem){
                                    timethread.cancel();
                                    timethread=null;
                                }


                            }
                            break;
                        case 5:
                            while (currentitem==5){
                                if(timethread==null){
                                    timethread=new timethread();
                                    timethread.start();
                                }


                                if(judvalue(currentitem,count)==1)
                                    count++;

                                if(count>7){
                                    Message msg = new Message();
                                    msg.what = 8;
                                    mHandler.sendMessage(msg);
                                    count=0;
                                    timethread.cancel();
                                    timethread=null;
                                }
                                tmpcurrent=currentitem;
                                getnewvalue();
                                if(tmpcurrent!=currentitem){
                                    timethread.cancel();
                                    timethread=null;
                                }


                            }
                            break;

                        }
                    }
                }

            }
        public  void cancel(){
            mjudthread.interrupt();
            judthreadstart=false;



        }
        private void getnewvalue(){
            values=FragmentHome.values;
            currentitem=TabActivity.currentitem;
            judgment=TabActivity.judgment;

            }
        private int judvalue(int currentitem,int cnt){
            if(Math.abs(values[3]-judgment[currentitem-1][cnt][3])<250&&Math.abs(values[4]-judgment[currentitem-1][cnt][4])<250)
                return 1;

            return 0;
            }
        }
    public class timethread extends Thread {
        public void run() { // override Thread's run()
            while (timethreadstart) {
                try {
                    Thread.sleep(6000);
                    if(count!=0 && Math.abs(FragmentHome.values[0]) < 5 ) count=0;
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void cancel(){
            count=0;
            timethread.interrupt();
            timethreadstart=false;



        }
    }

}


