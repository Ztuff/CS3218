package sg.edu.nus.cs3218project;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class CompassPlaybackFragment extends Fragment {
    long startTime;
    private TextView azimuthView;
    private TextView directionView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.compass_fragment, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        azimuthView = (TextView)getView().findViewById(R.id.compassDisplay);
        directionView = (TextView)getView().findViewById(R.id.compassDirection);
    }

    public int printToScreen(){
        ArrayList<Frame> compassHistory = MainActivity.getCompassHistory();
        startTime = ((PlaybackFragmentActivity)getActivity()).getStartTime();

        if(compassHistory.isEmpty() || startTime == 0){
            System.out.println("Uninitizalied");
            return 0;
        }


        for(Frame f: compassHistory){
            System.out.println("Direction: " + f.getDirection() + "    Degree:  " + f.getDegree());
        }

        Calendar c = Calendar.getInstance();
        int i = 0;
        //the time when the video started recording, plus the difference between now and then, minus the times of the first frame

        while(i != compassHistory.size()){
            Frame currentFrame = compassHistory.get(i);
            long frameTime = currentFrame.getTime();//The time between when the video started recording and when the degree was stored
            final int degree = currentFrame.getDegree();
            final String direction = currentFrame.getDirection();


            //System.out.println("Start time " + startTime + " System time " + System.currentTimeMillis() + "    History Time  " + (currentFrame.getTime() + timeDifference) + "   original Time  " + frameTime +  "    Degree " + degree);
            long timeDelay = frameTime - CalibrateActivity.delay;
            //System.out.println("Waiting: " + timeDelay);
            final Runnable setTextRunnable = new Runnable() {
                @Override
                public void run() {
                    azimuthView.setText(Integer.toString(degree));
                    directionView.setText(direction);
                }
            };

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(setTextRunnable);
                }
            };
            Timer timer = new Timer();

            if(timeDelay >= 0) {
                timer.schedule(timerTask, timeDelay);

            }
            i++;
        }
        return 1;
    }




}
