package sg.edu.nus.cs3218project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PlaybackFragmentActivity extends FragmentActivity {

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction;
    PlaybackFragment playbackFragment;
    CompassPlaybackFragment compassPlaybackFragment;
    AudioPlaybackActivity audioPlaybackActivity;
    boolean playing;
    long startTime;


    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_fragment);


        playing = false;
        startTime = 0;


        playbackFragment = new PlaybackFragment();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.video, playbackFragment);
        fragmentTransaction.commit();


        compassPlaybackFragment = new CompassPlaybackFragment();
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.compass, compassPlaybackFragment);
        fragmentTransaction.commit();

        audioPlaybackActivity = new AudioPlaybackActivity();
        audioPlaybackActivity.initializePlayer();


    }

    public void startPlaying(){
        compassPlaybackFragment.printToScreen();
        audioPlaybackActivity.startPlaying();
    }


    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public long getStartTime(){
        return startTime;
    }

}
