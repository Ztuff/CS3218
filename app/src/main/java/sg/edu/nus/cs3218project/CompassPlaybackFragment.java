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
        long timeDifference = startTime - compassHistory.get(0).getTime();
        while(i != compassHistory.size()){
            Frame currentFrame = compassHistory.get(i);
            long frameTime = currentFrame.getTime();
            System.out.println("System time " + System.currentTimeMillis() + "    History Time" + (currentFrame.getTime() + timeDifference));

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    azimuthView.setText(Long.toString(startTime));
                   // directionView.setText(currentFrame.getDirection());
                   }
            }, (startTime + frameTime + timeDifference));

            i++;


            //compassHistory.
            /*if(System.currentTimeMillis() == (currentFrame.getTime() + timeDifference)){
                System.out.println("i " + i);
                i++;
                azimuthView.setText(Integer.toString(currentFrame.getDegree()));
                directionView.setText(currentFrame.getDirection());
            }
            if(System.currentTimeMillis() > currentFrame.getTime()){
                i++;

            }*/

        }

        return 1;
    }

}
