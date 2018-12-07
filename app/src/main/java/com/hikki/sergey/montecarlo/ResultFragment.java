package com.hikki.sergey.montecarlo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hikki.sergey.montecarlo.component.Variable;
import com.hikki.sergey.montecarlo.utils.QuickSort;

import java.util.Random;

import com.hikki.sergey.montecarlo.utils.AppUtils;

public class ResultFragment extends Fragment {
    private static final String TAG = "ResultFragment";
    private static final String DIALOG_VND = "DialogVnd";

    private static final int REQUEST_VND = 0;

    private int mIters;
    private int mYears;
    private Variable mDiskont;
    private Variable mCapex;
    private double[][] mCapexList;
    private Variable mCashFlow;
    private double[][] mCashFlowList;
    private float mChance;

    private double[][] mNPV;
    private int mNPVi;

    private ProgressBar mStatusBar;
    private TextView mTitleTextView;
    private ImageView mNpvGraph;
    private Button mVndButton;
    private Button mIndexCapacity;
    private Button mSaveButton;

    private Bitmap mGraph;

    public static ResultFragment newIntent(int iters, int years, Variable diskont,
                                   Variable capex, double[] capexList, Variable cashFlow, double[] cashFlowList){
        Bundle args = new Bundle();
        args.putInt("iters", iters);
        args.putInt("years", years);
        args.putParcelable("diskont", diskont);
        args.putParcelable("capex", capex);
        args.putDoubleArray("capexList", capexList);
        args.putParcelable("cashFlow", cashFlow);
        args.putDoubleArray("cashFlowList", cashFlowList);

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mIters = getArguments().getInt("iters", 10000);
        mYears = getArguments().getInt("years", 1);
        mDiskont = (Variable) getArguments().getParcelable("diskont");
        mCapex = (Variable) getArguments().getParcelable("capex");
        mCashFlow = (Variable) getArguments().getParcelable("cashFlow");
        if (mCapex.getValue() == 0)
            mCapexList = AppUtils.getTwoDimensDoubleArr(getArguments().getDoubleArray("capexList"), mYears);
        if (mCashFlow.getValue() == 0)
            mCashFlowList = AppUtils.getTwoDimensDoubleArr(getArguments().getDoubleArray("cashFlowList"), mYears);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.result_fragment, container, false);

        mStatusBar = (ProgressBar) v.findViewById(R.id.progressBar);

        mTitleTextView = (TextView) v.findViewById(R.id.npv_text_view);

        mNpvGraph = (ImageView) v.findViewById(R.id.npv_image_view);

        mVndButton = (Button) v.findViewById(R.id.vnd_button);
        mVndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                double[] cf = new double[mNPV[mNPVi].length-2];
                System.arraycopy(mNPV[mNPVi],2, cf,0, mNPV[mNPVi].length-2);
                VndFragment dialog = VndFragment.newInstance(cf, mYears);
                dialog.setTargetFragment(ResultFragment.this, REQUEST_VND);
                dialog.show(manager, DIALOG_VND);
            }
        });
        mIndexCapacity = (Button) v.findViewById(R.id.index_capacity_button);
        mIndexCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Рассчитывается как и ВНД по наиболее частому рез-тату", Toast.LENGTH_SHORT).show();
            }
        });
        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.saveImage(mGraph, getActivity());
            }
        });

        setVisibleProgressBar(true);

        new CalculateMonteCarloAsyncTask().execute();
        return  v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            Log.i(TAG,"Result is not OK");
            return;
        }
        if (requestCode == REQUEST_VND){
            if (data == null){
                return;
            }

            double vnd = (double) data.getSerializableExtra(VndFragment.EXTRA_VND);
            Log.i(TAG,"VND: "+vnd);
            mVndButton.setText(getString(R.string.vnd, vnd+""));
        }
    }

    private void setVisibleProgressBar(boolean show){
        if (show){
            mStatusBar.setVisibility(View.VISIBLE);
            mTitleTextView.setVisibility(View.GONE);
            mNpvGraph.setVisibility(View.GONE);
            mVndButton.setVisibility(View.GONE);
            mIndexCapacity.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
        } else {
            mStatusBar.setVisibility(View.GONE);
            mTitleTextView.setVisibility(View.VISIBLE);
            mNpvGraph.setVisibility(View.VISIBLE);
            mVndButton.setVisibility(View.VISIBLE);
            mIndexCapacity.setVisibility(View.VISIBLE);
            mSaveButton.setVisibility(View.VISIBLE);
        }
    }

    private static double getRValue(double v, double minP, double maxP){
        Random rnd = new Random();
        Double d = rnd.nextDouble()*((minP + maxP)/100)*v + v*(100 - minP)/100;
        return d;
    }

    private void updateIndexCapacity(int k){
        double s = 0;
        for (int i = 0; i<mYears;i++){
            s+= mNPV[k][(i+1)*2 + 1];
        }
        s /= mNPV[k][0];
        mIndexCapacity.setText(getString(R.string.index_cash,String.format("%.3f",s)));
    }

    private class CalculateMonteCarloAsyncTask extends AsyncTask<Void,Void, double[][]>{

        @Override
        protected double[][] doInBackground(Void... params) {

            double[][] npv = new double[mIters][2+2*mYears];

            for(int k = 0; k < mIters; k++){
                double diskont = getRValue(mDiskont.getValue(), mDiskont.getLowDev(), mDiskont.getUpDev())/100;
                npv[k][1] = diskont;
                for (int year = 0; year < mYears; year++){
                    double year_sum = 0;

                    if (mCapexList != null){
                        for (int i = 0; i < mCapexList.length; i++){
                            year_sum -= getRValue(mCapexList[i][year],mCapex.getLowDev(),mCapex.getUpDev());
                            npv[k][(year+1)*2] += getRValue(mCapexList[i][year],mCapex.getLowDev(),mCapex.getUpDev());
                        }
                    } else {
                        year_sum -= getRValue(mCapex.getValue(),mCapex.getLowDev(),mCapex.getUpDev());
                        npv[k][(year+1)*2] += getRValue(mCapex.getValue(),mCapex.getLowDev(),mCapex.getUpDev());
                    }

                    if (mCashFlowList != null){
                        for (int i = 0; i < mCashFlowList.length;i++){
                            year_sum += getRValue(mCashFlowList[i][year],mCashFlow.getLowDev(),mCashFlow.getUpDev());
                            npv[k][(year+1)*2 + 1] += getRValue(mCashFlowList[i][year],mCashFlow.getLowDev(),mCashFlow.getUpDev());
                        }
                    } else {
                        year_sum += getRValue(mCashFlow.getValue(),mCashFlow.getLowDev(), mCashFlow.getUpDev());
                        npv[k][(year+1)*2 + 1] += getRValue(mCashFlow.getValue(),mCashFlow.getLowDev(), mCashFlow.getUpDev());
                    }

                    npv[k][0] += (year_sum)/Math.pow(1 + diskont/100, year);
                }
            }
            mChance = 0;
            for (int i = 0; i < npv.length; i++){
                if (npv[i][0] > 0)
                    mChance += 1.f;
            }
            mChance /= npv.length;

            npv = QuickSort.quickSort(npv);

            return npv;
        }

        @Override
        protected void onPostExecute(final double[][] result) {
            mNPV = result;
            mTitleTextView.setText(getString(R.string.vnd,"\n"+mChance));
            new RenderGraphAsyncTask().execute();
        }

    }

    private class RenderGraphAsyncTask extends AsyncTask<Void, Void, Void>{
        private static final int WIDTH = 1280;
        private static final int HEIGHT = 720;
        private static final int LEGEND_HEIGHT = 80;

        private static final int INDENT_FROM_EDGE = 30;
        private static final int LINE_THICKNESS = 3;

        private static final float TITLE_FONT_SIZE = 48.f;
        private static final float SUBTITLE_FONT_SIZE = 32.f;

        private int COLS = 75;

        private Bitmap Graph;
        private Canvas mCanvas;
        private Paint mPaint;

        @Override
        protected Void doInBackground(Void... params) {
            this.Graph = Bitmap.createBitmap(WIDTH, HEIGHT+LEGEND_HEIGHT, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(this.Graph);
            mPaint = new Paint();

            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);

            mCanvas.drawPaint(mPaint);

            drawRectDia();

            mPaint.setColor(Color.BLACK);

            mCanvas.drawRect(INDENT_FROM_EDGE * 4, INDENT_FROM_EDGE,INDENT_FROM_EDGE * 4 + LINE_THICKNESS, HEIGHT-INDENT_FROM_EDGE,mPaint);
            mPaint.setStrokeWidth(LINE_THICKNESS/1.4f);
            mCanvas.drawLine(INDENT_FROM_EDGE * 3.75f, INDENT_FROM_EDGE * 2,INDENT_FROM_EDGE * 4, INDENT_FROM_EDGE, mPaint);
            mCanvas.drawLine(INDENT_FROM_EDGE * 4.25f + LINE_THICKNESS, INDENT_FROM_EDGE * 2,INDENT_FROM_EDGE * 4 + LINE_THICKNESS, INDENT_FROM_EDGE,mPaint);

            mPaint.setTextSize(SUBTITLE_FONT_SIZE);
            mPaint.setTextAlign(Paint.Align.LEFT);
            mCanvas.drawText("%", INDENT_FROM_EDGE * 2.5f, INDENT_FROM_EDGE * 2, mPaint);

            mCanvas.drawRect(INDENT_FROM_EDGE * 4, HEIGHT-INDENT_FROM_EDGE-LINE_THICKNESS, WIDTH-INDENT_FROM_EDGE, HEIGHT-INDENT_FROM_EDGE, mPaint);
            mPaint.setStrokeWidth(LINE_THICKNESS/1.4f);
            mCanvas.drawLine(WIDTH-2*INDENT_FROM_EDGE, HEIGHT-1.25f*INDENT_FROM_EDGE-LINE_THICKNESS,WIDTH-INDENT_FROM_EDGE, HEIGHT-INDENT_FROM_EDGE-LINE_THICKNESS, mPaint);
            mCanvas.drawLine(WIDTH-2*INDENT_FROM_EDGE, HEIGHT-0.75f*INDENT_FROM_EDGE+LINE_THICKNESS,WIDTH-INDENT_FROM_EDGE, HEIGHT-INDENT_FROM_EDGE, mPaint);

            mPaint.setTextSize(SUBTITLE_FONT_SIZE);
            mPaint.setTextAlign(Paint.Align.LEFT);
            mCanvas.drawText("NPV",WIDTH-3.5f*INDENT_FROM_EDGE, HEIGHT+0.25f*INDENT_FROM_EDGE, mPaint);

            mPaint.setTextSize(TITLE_FONT_SIZE);
            mPaint.setTextAlign(Paint.Align.CENTER);

            mPaint.setAntiAlias(true);

            mPaint.setColor(Color.BLACK);

            mCanvas.drawText("Monte Carlo NPV",WIDTH/2.f, HEIGHT+LEGEND_HEIGHT-INDENT_FROM_EDGE/2.f-5, mPaint);

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            setVisibleProgressBar(false);
            mGraph = Graph;
            mNpvGraph.setImageBitmap(Graph);

            updateIndexCapacity(mNPVi);
        }

        private void drawRectDia(){
            mPaint.setColor(0xffffa500);
            double step = (mNPV[mIters-1][0] - mNPV[0][0])/COLS;
            int[] counts = new int[COLS];

            for (int i = 0; i < COLS; i++){
                for (int j = 0; j < mNPV.length; j++){
                    if ((mNPV[0][0]+i*step) <= mNPV[j][0]){
                        if (mNPV[j][0] <= (mNPV[0][0]+(i+1)*step)){
                            counts[i]++;
                        } else {
                            break;
                        }
                    }
                }
            }

            int countMax = counts[0];

            for (int i = 1; i < COLS; i++)
                if (countMax < counts[i])
                    countMax = counts[i];

            float coefY = (HEIGHT - INDENT_FROM_EDGE-LINE_THICKNESS)/(1.2f*countMax);
            float coefX = (WIDTH - 5 * INDENT_FROM_EDGE-LINE_THICKNESS)/(float) COLS;

            float x = 4.f * INDENT_FROM_EDGE+LINE_THICKNESS;
            float y = HEIGHT - INDENT_FROM_EDGE-LINE_THICKNESS;

            drawProc(countMax,y-coefY*countMax,y);
            int low = 0;
            int high = 0;
            int l = COLS / 2;
            double t = countMax;
            for (int i = 1; i < COLS; i++){
                if (t > 0.9*mIters){
                    break;
                } else {
                    if (i % 2 == 0){
                        t += counts[COLS/2 - (i+1)/2];
                        low = COLS/2 - (i+1)/2;
                    } else {
                        t += counts[COLS/2 + (i+1)/2];
                        high = COLS/2 + (i+1)/2;
                    }
                }
            }

            for (int i = 0; i < COLS; i++){
                mPaint.setColor(0xffffa500);
                mCanvas.drawRect(x+coefX*(i),y-coefY*counts[i],x+coefX*(i+1), y, mPaint);
                if (i% (COLS/5) == 0){
                    drawSign(x+coefX*(i+0.5f), y+INDENT_FROM_EDGE,  step, i);
                }
                if (countMax == counts[i]){
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(SUBTITLE_FONT_SIZE);
                    mPaint.setColor(Color.RED);
                    mCanvas.drawText(AppUtils.getScienceNum(step * i + mNPV[0][0]), x + coefX * (i + 0.5f), y - coefY * counts[i]-SUBTITLE_FONT_SIZE/2.f, mPaint);
                    countMax = -1;

                    double s = step * i + mNPV[0][0];
                    int k = 0;
                    for (int j = 1; j < mNPV.length; j++){
                        if (Math.abs(s-mNPV[j][0]) < Math.abs(s-mNPV[k][0])){
                            k = j;
                        }
                    }
                    mNPVi = k;
                } else if (low == i){
                    mPaint.setTextAlign(Paint.Align.RIGHT);
                    mPaint.setTextSize(SUBTITLE_FONT_SIZE);
                    mPaint.setColor(Color.BLUE);
                    mCanvas.drawText(AppUtils.getScienceNum(step * i + mNPV[0][0]), x + coefX * (i + 0.5f), y - coefY * counts[i]-SUBTITLE_FONT_SIZE/2.f, mPaint);
                } else if (high == i){
                    mPaint.setTextAlign(Paint.Align.LEFT);
                    mPaint.setTextSize(SUBTITLE_FONT_SIZE);
                    mPaint.setColor(Color.BLUE);
                    mCanvas.drawText(AppUtils.getScienceNum(step * i + mNPV[0][0]), x + coefX * (i + 0.5f), y - coefY * counts[i]-SUBTITLE_FONT_SIZE/2.f, mPaint);
                }
            }
        }

        private void drawSign(float x, float y, double step, int i){
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(SUBTITLE_FONT_SIZE);
            Double d = step * i + mNPV[0][0];
            mCanvas.drawRect(x-2,y-7.5f-INDENT_FROM_EDGE,x+2,y+7.5f-INDENT_FROM_EDGE, mPaint);
            mCanvas.drawText(AppUtils.getScienceNum(d), x, y+SUBTITLE_FONT_SIZE/2.f, mPaint);
        }

        private void drawProc(int max, float top, float bottom){
            int n = 5;
            mPaint.setTextAlign(Paint.Align.RIGHT);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(SUBTITLE_FONT_SIZE);
            for (int i = 0; i < n; i++){
                mCanvas.drawText(String.format("%.2f",max*100.f/mIters*(n-i)/n), 4*INDENT_FROM_EDGE-10,top+i*(bottom-top)/n, mPaint);
                mCanvas.drawRect(4*INDENT_FROM_EDGE-5,top+i*(bottom-top)/n-2, 4*INDENT_FROM_EDGE+5,top+i*(bottom-top)/n+2,mPaint);
            }

        }


    }
}