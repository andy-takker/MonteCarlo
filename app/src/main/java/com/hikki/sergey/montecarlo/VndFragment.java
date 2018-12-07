package com.hikki.sergey.montecarlo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class VndFragment extends DialogFragment {
    public static final String EXTRA_VND = "com.hikki.sergey.montecarlo.vnd";
    private static final String TAG = "VndFragment";

    private static final String ARG_CF = "vnd";
    private static final String ARG_YEAR = "year";

    private EditText mSecuritiesRate;
    private EditText mInflation;

    private View mSecuritiesRateWarning;
    private View mInflationWarning;

    private double[] mCF;
    private int mYear;

    public static VndFragment newInstance(double[] CF, int year){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CF, CF);
        args.putSerializable(ARG_YEAR, year);

        VndFragment fragment = new VndFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mCF = (double[]) getArguments().getSerializable(ARG_CF);
        mYear = (int) getArguments().getSerializable(ARG_YEAR);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.vnd_dialog_fragment, null);

        mSecuritiesRate = (EditText) v.findViewById(R.id.securities_rate);
        mSecuritiesRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mSecuritiesRateWarning.getVisibility() == View.VISIBLE){
                    mSecuritiesRateWarning.setVisibility(View.GONE);
                }
            }
        });
        mInflation = (EditText) v.findViewById(R.id.inflation);
        mInflation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mInflationWarning.getVisibility() == View.VISIBLE) {
                    mInflationWarning.setVisibility(View.GONE);
                }
            }
        });

        mSecuritiesRateWarning = (View) v.findViewById(R.id.securities_rate_warning);
        mSecuritiesRateWarning.setVisibility(View.GONE);

        mInflationWarning = (View) v.findViewById(R.id.inflation_warning);
        mInflationWarning.setVisibility(View.GONE);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Рассчет ВНД")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calculateVnd();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        return dialog;
    }

    private void calculateVnd(){
        double inflation;
        double securities_rate;
        try {
            securities_rate = Double.valueOf(mSecuritiesRate.getText().toString());
            if (securities_rate == 0 || securities_rate > 40){
                mSecuritiesRateWarning.setVisibility(View.VISIBLE);
                return;
            }
        } catch (Exception e){
            mSecuritiesRateWarning.setVisibility(View.VISIBLE);
            return;
        }
        try {
            inflation = Double.valueOf(mInflation.getText().toString());
            if (inflation == 0 || inflation > 40){
                mInflationWarning.setVisibility(View.VISIBLE);
                return;
            }
        } catch (Exception e){
            mInflationWarning.setVisibility(View.VISIBLE);
            return;
        }

        double res = inflation+securities_rate;
        Log.i(TAG,"Res0: "+res);
        while (true) {
            double npv = 0;
            for (int i = 0; i < mYear; i++){
                npv += (mCF[i]/Math.pow(1+res,i));
            }
            if (npv >= 0) {
                break;
            } else {
                res += 0.0001;
            }
        }
        sendResult(Activity.RESULT_OK,res);
    }
    private void sendResult(int resultCode, double res){
        if (getTargetFragment() == null){
            Log.i(TAG, "Have not target fragment");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_VND, res);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
