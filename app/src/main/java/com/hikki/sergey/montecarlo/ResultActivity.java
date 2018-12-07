package com.hikki.sergey.montecarlo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hikki.sergey.montecarlo.component.Variable;
import com.hikki.sergey.montecarlo.utils.AppUtils;

import java.util.List;

public class ResultActivity extends SingleFragmentActivity {
    private static final String ITERS_EXTRA = "iters";
    private static final String YEARS_EXTRA = "years";
    private static final String DISKONT_EXTRA = "diskont";
    private static final String CAPEX_EXTRA = "capex";
    private static final String CAPEX_LIST_EXTRA = "capexList";
    private static final String CASH_FLOW_EXTRA = "cashFlow";
    private static final String CASH_FLOW_LIST_EXTRA = "cashFlowList";


    public static Intent newInstance(Context packageContext, int iters, int years, Variable diskont,
                                     Variable capex, List<List<Double>> capexList, Variable cashFlow, List<List<Double>> cashFlowList){
        Intent intent = new Intent(packageContext, ResultActivity.class);
        intent.putExtra(ITERS_EXTRA, iters);
        intent.putExtra(YEARS_EXTRA, years);
        intent.putExtra(DISKONT_EXTRA, diskont);
        intent.putExtra(CAPEX_EXTRA, capex);
        intent.putExtra(CAPEX_LIST_EXTRA, AppUtils.getDoubleArray(capexList));
        intent.putExtra(CASH_FLOW_EXTRA, cashFlow);
        intent.putExtra(CASH_FLOW_LIST_EXTRA, AppUtils.getDoubleArray(cashFlowList));
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int iters = getIntent().getIntExtra(ITERS_EXTRA,1000);
        int years = getIntent().getIntExtra(YEARS_EXTRA,5);
        Variable diskont = (Variable) getIntent().getParcelableExtra(DISKONT_EXTRA);
        Variable capex = (Variable) getIntent().getParcelableExtra(CAPEX_EXTRA);
        double[] capexList = getIntent().getDoubleArrayExtra(CAPEX_LIST_EXTRA);
        Variable cashFlow = getIntent().getParcelableExtra(CASH_FLOW_EXTRA);
        double[] cashFlowList = getIntent().getDoubleArrayExtra(CASH_FLOW_LIST_EXTRA);

        return ResultFragment.newIntent(iters, years, diskont, capex, capexList, cashFlow, cashFlowList);
    }
}
