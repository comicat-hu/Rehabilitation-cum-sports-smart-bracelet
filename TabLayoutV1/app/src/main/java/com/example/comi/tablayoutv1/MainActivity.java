package com.example.comi.tablayoutv1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by Comi on 2015/11/21.
 */




import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {
    private DrawerLayout layDrawer;
    private ListView lstDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private Fragment fragment=null;
    // 記錄選擇過的的選單指標用
    private int CheckPosition=1;
    // 記錄被選擇的選單指標用
    private int mCurrentMenuItemPosition = 0;
    // 選單項目
    public static final String[] MENU_ITEMS = new String[] { "首頁","資料設定","清空紀錄" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
        fragment = new FragmentHome();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        initActionBar();
        initDrawer();
        initDrawerList();
    }

    private void initActionBar(){

        // 顯示 Up Button (位在 Logo 左手邊的按鈕圖示)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 打開 Up Button 的點擊功能
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void  initDrawer(){

        //指定drawer介面
        layDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lstDrawer = (ListView) findViewById(R.id.left_drawer);

        // 設定 Drawer 的影子
        //layDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        //取得目前標題


        drawerToggle = new ActionBarDrawerToggle( // 讓 Drawer Toggle 知道母體介面是誰
                this,
                layDrawer,
                toolbar,
                0,//打開時描述
                0) //關閉時描述
        {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (mCurrentMenuItemPosition > 0) {
                    getSupportActionBar().setTitle(MENU_ITEMS[mCurrentMenuItemPosition-1]);
                } else {
                    // 將 Title 設定回 APP 的名稱
                    getSupportActionBar().setTitle(R.string.app_name);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        //ActionBar 中的返回箭號置換成 Drawer 的三條線圖示。並且把這個觸發器指定給 layDrawer
        drawerToggle.syncState();

        layDrawer.setDrawerListener(drawerToggle);



    }
    private  void initDrawerList(){
        View header=getLayoutInflater().inflate(R.layout.drawer_header, null);
        lstDrawer.addHeaderView(header);

        lstDrawer.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        selectItem(position);
                       }


                }
        );
        // 設定清單的 Adapter，這裡直接使用 ArrayAdapter<String>
        lstDrawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, MENU_ITEMS));

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectItem(int position) {
        mCurrentMenuItemPosition = position;
        if(mCurrentMenuItemPosition==0) mCurrentMenuItemPosition++;

        if (mCurrentMenuItemPosition==CheckPosition)
            layDrawer.closeDrawer(lstDrawer);
        else if(mCurrentMenuItemPosition==3){
            new AlertDialog.Builder(this)
                    .setTitle("清除紀錄")
                    .setMessage("確定要清除紀錄?")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File f = new File(getFilesDir(), "data.txt");
                            if (f.exists()) {
                                f.delete();
                                Toast.makeText(getApplicationContext(), "清除成功", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "數據不存在", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else {
            switch (mCurrentMenuItemPosition) {

                case 1:
                    fragment = new FragmentHome();
                    break;
                case 2:
                    fragment = new FragmentItem1();
                    break;

                default:
                    //還沒製作的選項，fragment 是 null，直接返回
                    return;
            }
        }
            //[方法1]直接置換，無法按 Back 返回
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            //[方法2]開啟並將前一個送入堆疊
            //重要！ 必須加寫 "onBackPressed"
            if (CheckPosition==1){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, null)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

            }
            if(mCurrentMenuItemPosition==1){
                /*清空backstate*/
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < backStackCount; i++) {

                    // Get the back stack fragment id.
                    int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();

                    getSupportFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

            }




            // 將選單的子物件設定為被選擇的狀態

            lstDrawer.setItemChecked(mCurrentMenuItemPosition,true);
            if(mCurrentMenuItemPosition!=3)
            CheckPosition=mCurrentMenuItemPosition;
            // 關掉 Drawer
            layDrawer.closeDrawer(lstDrawer);
        }

    @Override
    public void onBackPressed() {
        CheckPosition=0;
        if (layDrawer.isDrawerOpen(lstDrawer)) {
            layDrawer.closeDrawer(lstDrawer);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Toast.makeText(this,"BACK",Toast.LENGTH_SHORT).show();
        }
        else{
            super.onBackPressed();
        }


    }
}

