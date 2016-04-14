package sg.edu.nus.cs3218project;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CalibrateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
    }
    int error = 0;
    public static long delay = 0;
    long i;
    int sensitivity = 20;

    public void btn_video_compass(View view) {
        float averageIntensity = 256;
        i = -1000;
        do {
            i += 1000;
            averageIntensity = averageIntensity();
        } while (averageIntensity < 128);

        if(error == 1) {
            Toast.makeText(getApplicationContext(), "Not calibrated: Please record a video before calibrating", Toast.LENGTH_LONG).show();
            return;
        }
        if(error == 2) {
            Toast.makeText(getApplicationContext(), "Not calibrated: Average color intensity never got high enough", Toast.LENGTH_LONG).show();
            return;
        }
        if(i == 0) {
            Toast.makeText(getApplicationContext(), "Not calibrated: Start of video must be dark, average color intensity of first frame measured as " + averageIntensity + "/255", Toast.LENGTH_LONG).show();
            return;
        }

        String time = formatTimeMs(i);
        String avg = String.format("%.2f", averageIntensity);
        Toast.makeText(getApplicationContext(), "Calibration event found in video at " + time + ", where an average color intensity of " + avg + "/255 was measured", Toast.LENGTH_LONG).show();
        int compassEventIndex = compassEventFrame();
        if(compassEventIndex == -1){
            Toast.makeText(getApplicationContext(), "Not calibrated: Please turn more than 10 degrees", Toast.LENGTH_LONG).show();
        }
        Frame eventFrame = MainActivity.getCompassHistory().get(compassEventIndex);
        long eventTime = eventFrame.getTime();
        time = formatTimeMs(eventFrame.getTime());
        delay = eventTime - i;
        Toast.makeText(getApplicationContext(), "Calibration event found in compass history at " + time + ". Delay: " + delay + "ms", Toast.LENGTH_LONG).show();
    }

    private String formatTimeMs(long i) {
        long ms = i % 1000;
        long s = (i / 1000) % 60;
        long m = (i / (1000 * 60)) % 60;
        return String.format("%02d:%02d:%03d", m, s, ms);
    }

    private int compassEventFrame() {
        // Pass by value, not by reference
        ArrayList<Frame> compassHistory = new ArrayList<>(MainActivity.getCompassHistory());
        int size = compassHistory.size();
        int minDegree = 180, maxDegree = -180;
        // Check for jumps between -180 and 180
        for (int j = 0; j < size; j++){
            if(compassHistory.get(j).getDegree() < minDegree)
                minDegree = compassHistory.get(j).getDegree();
            else if(compassHistory.get(j).getDegree() > minDegree)
                minDegree = compassHistory.get(j).getDegree();
        }
        // If jumps detected, add 360 to all negative numbers, to start the cycle half way through
        if(minDegree < -90 && maxDegree > 90)
        {
            for (int j = 0; j < size; j++) {
                Frame frame = compassHistory.get(j);
                if(frame.getDegree() < 0)
                    compassHistory.set(j, new Frame(frame.getDirection(), frame.getDegree() + 360, frame.getTime()));
            }
        }
        // Smooth out the results by making them an average of the closest five samples
        int[] degrees = new int[size];
        for (int j = 0; j < size; j++){
            int deg1 = j - 2 < 0 ? compassHistory.get(j).getDegree() : compassHistory.get(j - 2).getDegree();
            int deg2 = j - 1 < 0 ? compassHistory.get(j).getDegree() : compassHistory.get(j - 1).getDegree();
            int deg3 = compassHistory.get(j).getDegree();
            int deg4 = j + 1 >= size ? compassHistory.get(j).getDegree() : compassHistory.get(j + 1).getDegree();
            int deg5 = j + 2 >= size ? compassHistory.get(j).getDegree() : compassHistory.get(j + 2).getDegree();
            degrees[j] = (deg1 + deg2 + deg3 + deg4 + deg5)/5;
        }

        int[] first5 = new int[5], last5 = new int[5];
        for(int j = 0; j < first5.length; j++){
            first5[j] = degrees[j];
            last5[last5.length - 1 - j] = degrees[size - 1 - j];
        }
        Arrays.sort(first5);
        Arrays.sort(last5);
        int startDegree = first5[2];

        int retVal = -1;
        for (int j = 0; j < size; j++){
            int diff = Math.abs(degrees[j] - startDegree);
            if (diff > sensitivity) {
                retVal = j;
                break;
            }
        }
        return retVal;
    }

    private float averageIntensity() {
        Bitmap frame = getFrameAtTime(i);
        if(frame == null)
            return 256;
        float totalIntensity = 0;
        int pixelCount = 0;

        for (int y = 0; y < frame.getHeight(); y++) {
            for (int x = 0; x < frame.getWidth(); x++) {
                int c = frame.getPixel(x, y);
                pixelCount++;
                totalIntensity += Math.max(Color.red(c), Math.max(Color.green(c),Color.blue(c)));
            }
        }

        float averageIntensity = totalIntensity / pixelCount;

        // Event found, fine tune
        if(averageIntensity >= 128 && i != 0){
            int half = 500;
            i -= half;
            Bitmap lastFrame = null;
            while(!frame.sameAs(lastFrame) || averageIntensity < 128) {
                lastFrame = frame;
                frame = getFrameAtTime(i);
                totalIntensity = 0;
                pixelCount = 0;

                for (int y = 0; y < frame.getHeight(); y++) {
                    for (int x = 0; x < frame.getWidth(); x++) {
                        int c = frame.getPixel(x, y);
                        pixelCount++;
                        totalIntensity += Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                    }
                }
                half = half/2 + 1; // +1 avoids deadlock by 0 incrementation
                averageIntensity = totalIntensity / pixelCount;
                if(averageIntensity < 128)
                    i += half;
                else
                    i -= half;
            }
        }
        return averageIntensity;
    }

    public Bitmap getFrameAtTime(long timeMs) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(CamcorderView.outputDir + "/" + CamcorderView.outputFilename);
        }
        catch(Exception e){
            error = 1;
            return null;
        }
        if(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) > timeMs)
            return retriever.getFrameAtTime(timeMs * 1000);
        else {
            error = 2;
            return null;
        }
    }
}
