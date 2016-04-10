package sg.edu.nus.cs3218project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class PlaybackActivity extends Activity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setVideoPath("/sdcard/CS3218/Videos/fileName.mp4");

        videoView.start();
    }

    public void btn_play(View view) {
        videoView.start();
    }
}
