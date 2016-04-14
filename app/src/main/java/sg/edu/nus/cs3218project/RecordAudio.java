package sg.edu.nus.cs3218project;

import android.media.AudioRecord;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Reem on 2016-04-14.
 */
public class RecordAudio{
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
    public static long endTimeStamp = 0;
    public boolean runFlag = false;
    private ArrayList<short[]> samples = new ArrayList<>();
    public static short[] recording;

    public RecordAudio(){

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

        audioRecord.startRecording();
        runFlag = true;



        recordingThread = new Thread()
        {
            public void run()
            {
                /*boolean firstFlag = true;
                int firstAmpSum = 0;
                int lastAmpSum;*/

                while (runFlag) {
                    audioRecord.read(buffer, 0, bufferSize);
                    samples.add(buffer.clone());
                    /*if(firstFlag){
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
                            endTimeStamp = System.currentTimeMillis() - startTimeStamp;
                        }
                    }*/
                }
            }
        };
        recordingThread.start();
        startTimeStamp = System.currentTimeMillis();

    }

    public void stop() {
        runFlag = false;
        try {
            recordingThread.join();
        }
        catch(InterruptedException e){};
        audioRecord.stop();
        audioRecord.release();

        recording = new short[samples.size()*bufferSize];
        int index = 0;
        for (int i = 0; i < samples.size(); i++){
            short[] sample = samples.get(i);
            for(int j = 0; j < bufferSize; j++){
                    recording[index++] = sample[j];
            }
        }

        int recordingSize = 0;
        for(int i = 0; i < recording.length; i++){
            if(recording[i] != 0)
                recordingSize++;
        }
        short[] recordingStripped = new short[recordingSize];
        index = 0;
        for(int i = 0; i < recording.length; i++){
            if(recording[i] != 0)
                recordingStripped[index++] = recording[i];
        }
        recording = recordingStripped;
    }
}
