package sg.edu.nus.cs3218project;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Reem on 2016-04-14.
 */
public class AudioPlaybackActivity extends Activity{

    private MediaPlayer mPlayer = null;
    private String fileUrl = CamcorderView.outputDir.getAbsolutePath() + "/" + RecordAudio.outputFilename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initializePlayer(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileUrl);
            mPlayer.prepare();
        } catch (IOException e) {
            Log.e("audio_playback_error", "prepare() failed");
        }

    }

    public void startPlaying() {
        mPlayer.start();
    }

}
