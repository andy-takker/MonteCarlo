package com.hikki.sergey.montecarlo.component;

import android.os.Parcel;
import android.os.Parcelable;

public class Variable implements Parcelable {

    private double value;
    private double lowDev;
    private double upDev;

    public Variable(){
    }
    public Variable(Parcel in){
        double[] data = new double[3];
        in.readDoubleArray(data);

        this.value = data[0];
        this.lowDev = data[1];
        this.upDev = data[2];
    }


    public void setValue(double value) {
        this.value = value;
    }

    public void setDevs(double up, double low){
        lowDev = low;
        upDev = up;
    }

    public double getValue() {
        return value;
    }

    public double getLowDev() {
        return lowDev;
    }

    public double getUpDev() {
        return upDev;
    }

    public boolean isConstant(){
        return lowDev == 0 && upDev == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray( new double[]{this.value, this.lowDev, this.upDev});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Variable createFromParcel(Parcel in) {
            return new Variable(in);
        }

        public Variable[] newArray(int size) {
            return new Variable[size];
        }
    };
}
