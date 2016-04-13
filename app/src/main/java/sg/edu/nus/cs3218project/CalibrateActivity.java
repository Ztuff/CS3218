package sg.edu.nus.cs3218project;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CalibrateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
    }
    int error = 0;

    public void btn_video_compass(View view) {
        float averageIntensity = 256;
        int i = -1000;
        do {
            i += 1000;

            Bitmap frame = getFrameAtTime(i);
            if(frame == null)
                break;
            float totalIntensity = 0;
            int pixelCount = 0;

            for (int y = 0; y < frame.getHeight(); y++) {
                for (int x = 0; x < frame.getWidth(); x++) {
                    int c = frame.getPixel(x, y);
                    pixelCount++;
                    totalIntensity += Math.max(Color.red(c), Math.max(Color.green(c),Color.blue(c)));
                }
            }

            averageIntensity = totalIntensity / pixelCount;
            
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
        } while (averageIntensity < 128);

        if(error == 1)
            Toast.makeText(getApplicationContext(), "Not calibrated: Please record a video before calibrating", Toast.LENGTH_LONG).show();
        else if(error == 2)
            Toast.makeText(getApplicationContext(), "Not calibrated: Average color intensity never got high enough", Toast.LENGTH_LONG).show();
        else if(i == 0)
            Toast.makeText(getApplicationContext(), "Not calibrated: Start of video must be dark, average color intensity of first frame measured as " + averageIntensity + "/255", Toast.LENGTH_LONG).show();
        else {
            int ms = i % 100;
            int s = (i / 1000) % 60;
            int m = (i / (1000 * 60)) % 60;
            String time = String.format("%02d:%02d:%d", m, s, ms);
            String avg = String.format("%.2f", averageIntensity);
            Toast.makeText(getApplicationContext(), "Calibration event found in video at " + time + ", where an average color intensity of " + avg + "/255 was measured", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getFrameAtTime(int timeMs) {
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
