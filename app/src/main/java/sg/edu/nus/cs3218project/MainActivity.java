package sg.edu.nus.cs3218project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent i;
    ArrayList<Frame> compassHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compassHistory = new ArrayList<Frame>();
    }

    public void btn_record(View view) {
        int requestCode = 1;
        i = new Intent(this, RecordFragmentActivity.class);
        startActivityForResult(i, requestCode);
    }

    public void btn_playback(View view) {
        i = new Intent(this, PlaybackFragmentActivity.class);
        i.putExtra("mylist", compassHistory);
        startActivity(i);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==1) {
            Bundle extras = data.getExtras();

            compassHistory = extras.getParcelableArrayList("myarraylist");
        }
    }
}
