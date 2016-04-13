package sg.edu.nus.cs3218project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;


/**
 * Created by Reem on 2016-04-13.
 */
public class RecordFragmentActivity extends FragmentActivity {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction;
    boolean recording;
    ArrayList<Frame> compassHistory;


    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordfragment);

        recording = false;

        CameraFragment textFragment = new CameraFragment();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.video, textFragment);
        fragmentTransaction.commit();


        CompassFragment compassFragment = new CompassFragment();
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.compass, compassFragment);
        fragmentTransaction.commit();


    }

    public void setCompassHistory(ArrayList<Frame> compassHistory){
        this.compassHistory = compassHistory;
    }

    public ArrayList<Frame> getCompassHistory(){
       // if(compassHistory == null){
        //    return null;
        //}
        Intent intent= getIntent();
        Bundle b = new Bundle();
        b.putParcelableArrayList("myarraylist", compassHistory);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();

        return compassHistory;
    }

    public void setRecording(boolean recording){
        this.recording = recording;
    }

    public boolean getRecording(){
        return recording;
    }
}
