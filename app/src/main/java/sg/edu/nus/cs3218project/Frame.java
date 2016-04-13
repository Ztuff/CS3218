package sg.edu.nus.cs3218project;

/**
 * Created by Reem on 2016-04-12.
 */
public class Frame {

    private String direction;
    private int degree;
    private long time;

    public Frame(){}

    public Frame(String direction, int degree, long time){
        this.direction = direction;
        this.degree = degree;
        this.time = time;
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
