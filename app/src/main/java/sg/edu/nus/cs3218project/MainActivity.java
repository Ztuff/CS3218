package sg.edu.nus.cs3218project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn_record(View view) {
        i = new Intent(this, RecordFragmentActivity.class);
        startActivity(i);
    }

    public void btn_playback(View view) {
        i = new Intent(this, PlaybackActivity.class);
        startActivity(i);
    }
}
