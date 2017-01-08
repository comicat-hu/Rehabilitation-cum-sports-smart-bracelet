package com.example.comi.tablayoutv1;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

     ImageView gifView;

    static AnimationDrawable anim = null;

    private int[] ID = new int[]{R.drawable.gif1, R.drawable.gif2,R.drawable.gif3,R.drawable.gif4,R.drawable.gif5,0,0,0};
    private int[] playID = new int[]{R.drawable.action1play,R.drawable.action2play,R.drawable.action3play,R.drawable.action4play,R.drawable.action5play,0,0,0};

    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static MainActivityFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        //textView.setText("NO." + mPage + "#");
       // RelativeLayout layout_gif = (RelativeLayout)view.findViewById(R.id.layout_gif);

        gifView = (ImageView) view.findViewById(R.id.gifView);

        if(playID[mPage-1] != 0)
        gifView.setBackgroundResource(playID[mPage-1]);


        gifView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                if(ID[mPage-1] != 0)
                    gifView.setBackgroundResource(ID[mPage-1]);
                anim = (AnimationDrawable) gifView.getBackground();

                if(anim.isRunning()){
                    anim.stop();
                    if(playID[mPage-1] != 0)
                        gifView.setBackgroundResource(playID[mPage-1]);
                }
                else{
                    if(ID[mPage-1] != 0)
                        gifView.setBackgroundResource(ID[mPage-1]);
                    anim.start();
                }

            }
        });


        return view;
    }


}
