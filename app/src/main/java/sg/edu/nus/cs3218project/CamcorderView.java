package sg.edu.nus.cs3218project;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

public class CamcorderView extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mHolder;

    private Camera mCamera;
    private MediaRecorder mRecorder;
    public static File outputDir = new File("/sdcard/CS3218/Videos/");
    public static String outputFilename = "fileName.mp4";
    public static long startTime;
    boolean initialized = false;

    public CamcorderView(Context context) {
        super(context);

        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);

        mCamera = Camera.open();
        mRecorder = new MediaRecorder();

    }

    public void start() {
        mCamera.setDisplayOrientation(90);
        mCamera.unlock();
        mRecorder.setOrientationHint(90);
        mRecorder.setCamera(mCamera);

        mRecorder.setPreviewDisplay(mHolder.getSurface());

        mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

//        File outputFile = new File(outputDir, new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date()) + ".mp4");
        File outputFile = new File(outputDir, outputFilename);
        mRecorder.setOutputFile(outputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
        startTime = System.currentTimeMillis();
        mRecorder.start();
    }

    public void stop() {
        mRecorder.reset();
        mCamera.lock();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        outputDir.mkdirs();
    }
}