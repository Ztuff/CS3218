package sg.edu.nus.cs3218project;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Reem on 2016-04-12.
 */
public class Frame implements Parcelable{

    private String direction;
    private int degree;
    private long time;

    public Frame(){}

    public Frame(String direction, int degree, long time){
        this.direction = direction;
        this.degree = degree;
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(direction);
        dest.writeInt(degree);
        dest.writeLong(time);
    }


    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection(){
        return direction;
    }

    public void setDegree(int degree) {
        this.degree = degree;

    }

    public int getDegree(){
        return degree;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime(){
        return time;
    }
}
