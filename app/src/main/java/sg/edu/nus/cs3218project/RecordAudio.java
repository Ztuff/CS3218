package sg.edu.nus.cs3218project;

import android.media.AudioRecord;
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
    private static final int  FS = 16000;     // sampling frequency
    public AudioRecord audioRecord;
    private int               audioEncoding = 2;
    private int               nChannels = 16;
    private Thread            recordingThread;
    public  static short[]  buffer;
    public  static int      bufferSize;
    private long startTimeStamp;
    public static long endTimeStamp;

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

        try {
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));
        }
        catch (Exception e) {
            Log.d("Error in Init() ", e.getMessage());
        }

        bufferSize = AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding);
        buffer = new short[bufferSize];

        mRecorder.start();
        audioRecord.startRecording();



        recordingThread = new Thread()
        {
            public void run()
            {
                boolean firstFlag = true;
                int firstAmpSum = 0;
                int lastAmpSum;

                while (true) {
                    audioRecord.read(buffer, 0, bufferSize);
                    if(firstFlag){
                        for(int i = 0; i < bufferSize; i++){
                            firstAmpSum += Math.abs(buffer[i]);

                        }
                        firstAmpSum = firstAmpSum/bufferSize;
                        firstFlag = false;
                    }
                    else{
                        lastAmpSum = 0;
                        for(int i = 0; i < bufferSize; i++){
                            lastAmpSum += Math.abs(buffer[i]);

                        }
                        lastAmpSum = lastAmpSum/bufferSize;

                        if(lastAmpSum > firstAmpSum * 10){
                            endTimeStamp = startTimeStamp - System.currentTimeMillis();
                        }
                    }
                }
            }
        };
        recordingThread.start();
        startTimeStamp = System.currentTimeMillis();

    }

    public void stop() {
        mRecorder.reset();
        recordingThread.stop();
        audioRecord.stop();
        audioRecord.release();
    }
}
