package com.example.comi.tablayoutv1;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MkHo on 2015/10/13.
 */
public class FragmentItem1 extends Fragment {
    View v;
    Button save;
    RadioButton men,women;
    EditText namevalue;
    RadioGroup Rdgroup;
    int gender;
    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String nameField = "NAME";
    private static final String genderField = "gender";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_item1, container, false);

        initComponent();
        readData();
        return v;


    }
    public void initComponent() {
        namevalue = (EditText) v.findViewById(R.id.namevalue);
        men = (RadioButton) v.findViewById(R.id.men);
        women = (RadioButton) v.findViewById(R.id.women);
        Rdgroup = (RadioGroup) v.findViewById(R.id.Rdgroup);
        Rdgroup.setOnCheckedChangeListener(listener);
        save = (Button) v.findViewById(R.id.savesetting);
        save.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
            saveData();

            }
        });
    }
    public void readData(){
        settings = getActivity().getSharedPreferences(data,0);
        namevalue.setText(settings.getString(nameField, ""));
        gender=settings.getInt(genderField,0);

        if (gender==1)
            men.setChecked(true);
        else if(gender==2)
            women.setChecked(true);
    }
    public void saveData(){
        settings = getActivity().getSharedPreferences(data,0);
        settings.edit()
                .putString(nameField, namevalue.getText().toString())
                .putInt(genderField, gender)
                .commit();
        Toast.makeText(getActivity(), "保存設定", Toast.LENGTH_SHORT).show();
    }
    private RadioGroup.OnCheckedChangeListener listener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int p = group.indexOfChild((RadioButton) v.findViewById(checkedId));
            int count = group.getChildCount();
            switch (checkedId){
                case R.id.men:
                    gender=1;
                    break;
                case R.id.women:
                    gender=2;
                    break;

            }

        }
    };

}