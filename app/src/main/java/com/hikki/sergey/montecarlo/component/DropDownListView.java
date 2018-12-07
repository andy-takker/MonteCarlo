package com.hikki.sergey.montecarlo.component;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DropDownListView extends Spinner {
    private Context mContext;
    private int mValueCoef;
    private List<String> mSpinnerArray;

    private static final String DDLV_1 = "един.";
    private static final String DDLV_2 = "тысяч.";
    private static final String DDLV_3 = "милл.";

    public DropDownListView(Context context) {
        super(context, Spinner.MODE_DROPDOWN);
        mContext = context;

        mSpinnerArray = new ArrayList<>();
        mSpinnerArray.add(DDLV_1);
        mSpinnerArray.add(DDLV_2);
        mSpinnerArray.add(DDLV_3);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, mSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(adapter);

        this.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                setValueCoef(item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public int getValueCoef() {
        return mValueCoef;
    }
    private void setValueCoef(String s){
        if (s.equals(DDLV_2)){
            mValueCoef = 1000;
        } else if (s.equals(DDLV_1)){
            mValueCoef = 1;
        } else {
            mValueCoef = 1000000;
        }
    }
}
