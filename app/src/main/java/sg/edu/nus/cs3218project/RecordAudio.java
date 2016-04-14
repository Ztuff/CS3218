package sg.edu.nus.cs3218project;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Reem on 2016-04-14.
 */
public class RecordAudio{
    private MediaRecorder mRecorder;
    private File outputDir = CamcorderView.outputDir;
    public static String outputFilename = "audio.mp4";
    public static long startTime;

    public RecordAudio(){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        File outputFile = new File(outputDir, outputFilename);
        mRecorder.setOutputFile(outputFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
    }

    public void start(){
        mRecorder.start();
    }

    public void stop() {
        mRecorder.reset();
    }
}
