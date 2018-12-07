package com.hikki.sergey.montecarlo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.hikki.sergey.montecarlo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AppUtils {
    public static double[] getDoubleArray(List<List<Double>> list){
        if (list != null){
            int n = list.size()*list.get(0).size();
            double[] res = new double[n];

            for(int i = 0; i< list.size(); i++)
                for (int j = 0; j < list.get(i).size(); j++)
                    res[i*list.get(i).size()+j] = list.get(i).get(j);

            return res;
        }
        return null;
    }
    public static double[][] getTwoDimensDoubleArr(double[] arr, int cols){
        if (arr == null)
            return null;
        int rows = arr.length/cols;
        double[][] res = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                res[i][j] = arr[i*cols + j];

        return res;
    }
    public static String getScienceNum(Double n){
        StringBuilder sb = new StringBuilder();
        int k = 0;
        int t = n < 0 ? 1 : 0;
        while (n.toString().charAt(t) == 0){
            n *= 10;
            k++;
        }
        if (t == 1)
            sb.append(n.toString().charAt(0));
        sb.append(n.toString().charAt(t));
        sb.append('.');
        sb.append(n.toString().charAt(1+t));
        sb.append(n.toString().charAt(2+t));

        if (k != 0){
            sb.append("E-").append(k);
        } else {
            for (int i = 1+t; i < n.toString().length(); i++){
                if (n.toString().charAt(i) != '.'){
                    k++;
                } else {
                    sb.append("E").append(k);
                    break;
                }
            }
        }
        return sb.toString();
    }
    public static void saveImage(Bitmap image, Context context){
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/MonteCarlo");
            dir.mkdirs();
            String fileName = context.getString(R.string.file_name,System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            FileOutputStream outStream = new FileOutputStream(outFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            context.sendBroadcast(intent);
            Toast.makeText(context, "File "+outFile.getName()+" was saved.", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe){
            Toast.makeText(context, ioe.getMessage()+"", Toast.LENGTH_SHORT).show();
        }


    }
}
