package sg.edu.nus.cs3218project;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import java.util.Calendar;

public class PlaybackFragment extends Fragment implements View.OnClickListener{
    VideoView videoView;


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        videoView = (VideoView)getView().findViewById(R.id.videoView);
        videoView.setVideoPath("/sdcard/CS3218/Videos/fileName.mp4");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_playback, container, false);

        // Create our Preview view and set it as the content of our activity.
        Button upButton = (Button) view.findViewById(R.id.play);
        upButton.setOnClickListener(this);

        return view;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

    }

    public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        long time = System.currentTimeMillis();
        ((PlaybackFragmentActivity)getActivity()).setStartTime(time);
        ((PlaybackFragmentActivity)getActivity()).startPlaying();

        videoView.start();



    }
}
