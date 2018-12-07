package com.hikki.sergey.montecarlo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hikki.sergey.montecarlo.component.TitleContainerView;
import com.hikki.sergey.montecarlo.component.ValueWithDetailListView;
import com.hikki.sergey.montecarlo.component.DiscountRateView;
import com.hikki.sergey.montecarlo.component.Variable;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TitleContainerView mTitleContainerView;
    private DiscountRateView mDiscountRateView;
    private ValueWithDetailListView mCapexView;
    private ValueWithDetailListView mCashFlow;
    private Button mCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar();
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);

        LinearLayout rootView = new LinearLayout(this);
        rootView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(rootView);

        mTitleContainerView = new TitleContainerView(this);
        mTitleContainerView.getItersEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitleContainerView.showHideErrorMessIter(s);
            }
        });
        mTitleContainerView.getYearEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitleContainerView.showHideErrorMessYear(s);
                updateYearsDetailsViews();
            }
        });

        mDiscountRateView = new DiscountRateView(this);

        mCapexView = new ValueWithDetailListView(this);
        mCapexView.setTitle(R.string.capex);

        mCashFlow = new ValueWithDetailListView(this);
        mCashFlow.setTitle(R.string.cash);

        mCalculate = new Button(this);
        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iters = mTitleContainerView.getIters();
                int years = mTitleContainerView.getYears();

                Variable diskont = mDiscountRateView.getDiskountRate();

                Variable capex = mCapexView.getVariable();
                List<List<Double>> _capex = mCapexView.getDetailListView();

                Variable cashFlow = mCashFlow.getVariable();
                List<List<Double>> _cashFlow = mCashFlow.getDetailListView();

                Intent intent = ResultActivity.newInstance(MainActivity.this, iters, years, diskont, capex, _capex, cashFlow, _cashFlow);
                startActivity(intent);
            }
        });
        mCalculate.setTypeface(Typeface.SERIF);
        mCalculate.setText("Рассчитать");

        rootView.addView(mTitleContainerView);
        rootView.addView(mDiscountRateView);
        rootView.addView(mCapexView);
        rootView.addView(mCashFlow);

        rootView.addView(mCalculate);

        updateYearsDetailsViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_info:
                createInfoFragment();
                return true;
            default:
                return false;
        }

    }

    private void createInfoFragment(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setTitle("Info")
                .setMessage("Какое-то очень умное пояснение работы программы... Долго работали, зачем непонятно)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.image_info_dark)
                .show();
    }

    private void updateYearsDetailsViews(){
        mCapexView.setYears(mTitleContainerView.getYears());
        mCapexView.updateDetails();

        mCashFlow.setYears(mTitleContainerView.getYears());
        mCashFlow.updateDetails();
    }


}
