package sg.edu.nus.cs3218project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

public class PlaybackFragmentActivity extends FragmentActivity {

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction;
    boolean playing;
    long startTime;
    ArrayList<Frame> compassHistory;


    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_fragment);


        playing = false;
        compassHistory = getIntent().getParcelableArrayListExtra("mylist");


        PlaybackFragment playbackFragment = new PlaybackFragment();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.video, playbackFragment);
        fragmentTransaction.commit();


        CompassPlaybackFragment compassPlaybackFragment = new CompassPlaybackFragment();
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.compass, compassPlaybackFragment);
        fragmentTransaction.commit();


    }

    public ArrayList<Frame> getCompassHistory(){
        return compassHistory;
    }

    public void setPlaying(boolean playing){
        this.playing = playing;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public long getStartTime(){
        return startTime;
    }

}
