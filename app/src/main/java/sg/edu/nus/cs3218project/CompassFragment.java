package sg.edu.nus.cs3218project;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Reem on 2016-04-06.
 */
public class CompassFragment extends Fragment implements SensorEventListener {

    static final float FILTER_ALPHA = 0.25f;


    private SensorManager sensorManager;
    private Sensor accelerometre;
    private Sensor magnetometre;
    private TextView azimuthView;
    private TextView directionView;
    private ImageView image;


    private float[] mGravity;
    private float[] mGeomagnetic;

    private ArrayList<Frame> compassHistory;
    int frames;

    private int azimuth;
    private String direction;

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
        //image = (ImageView) getView().findViewById(R.id.imageViewCompass);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compassHistory = new ArrayList<Frame>();
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometre = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        azimuth = 0;
        frames = 0;

    }

    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometre, SensorManager.SENSOR_DELAY_UI);
    }

    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    protected float[] lowPassFilter(float[] inputData, float[] outputData) {
        if (outputData == null){
            return inputData;
        }
        for (int i = 0; i < inputData.length; i++) {
            outputData[i] = outputData[i] + FILTER_ALPHA * (inputData[i] - outputData[i]);
        }
        return outputData;
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            mGeomagnetic = lowPassFilter(event.values.clone(), mGeomagnetic);
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = lowPassFilter(event.values.clone(), mGravity);
        }

        if((mGravity != null) && (mGeomagnetic != null)){
            float R[] = new float[9];
            float I[] = new float[9];


            if(SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)){
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                //orientaiton array contains azimuth, pitch, and roll
                azimuth = (int) Math.round(Math.toDegrees(orientation[0]));
                direction = getDirection((float)(azimuth));

                if(((RecordFragmentActivity)getActivity()).getRecording()){
                    setFrameInHistory(azimuth, direction);
                    if(frames%100 == 0){
                        System.out.println(compassHistory.get(frames).getDegree() + "     " + compassHistory.get(frames).getDirection()
                                + "    " + compassHistory.get(frames).getTime());
                    }

                    frames++;

                }
                else{
                    ((RecordFragmentActivity)getActivity()).setCompassHistory(compassHistory);
                }

                azimuthView.setText(Integer.toString(azimuth));
                directionView.setText(direction);

            }
        }
    }

    private void setFrameInHistory(int azimuth, String direction){

        Calendar c = Calendar.getInstance();
        long time = System.currentTimeMillis();
        Frame frame = new Frame(direction, azimuth, time);
        compassHistory.add(frame);


    }

    public static String getDirection(float degrees)
    {
        degrees = degrees * 10;

        if(degrees < 0){
            degrees = 1800 + (1800 + degrees);
        }


        String[] cardinals = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N" };
        int position = (int) Math.round(((double) Math.abs(degrees) % 3600) / 225);
        //System.out.println("Degrees" + degrees + "Position " + position + "    Direction: " + cardinals[position]);
        return cardinals[position];
    }


}
