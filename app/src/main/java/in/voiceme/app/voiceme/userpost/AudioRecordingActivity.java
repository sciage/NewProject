package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class AudioRecordingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AudioFxActivity";

    private static final float VISUALIZER_HEIGHT_DIP = 360f;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder myAudioRecorder;
    private TextView timer;
    private String time;
    public boolean isContinue = true;
    private int maxDuration = 120 * 1000;

    private static final String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + "currentRecording.mp3";
    private int currentDuration = 0;
    private int second = 0;
    private int minute = 0;
    private String timePlay;
    private boolean isListen = false;

    private ImageView play;
    private ImageView stop;
    private ImageView record;
    private ImageView done;



    Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        public void run() {
            handler.postDelayed(updateThread, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);
        getSupportActionBar().setTitle("LoginUser Feelings");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // filePath = Environment.getExternalStorageDirectory() + "/currentRecording.mp3";

        timer = (TextView) findViewById(R.id.timer_tv);
        play = (ImageView) findViewById(R.id.play);
        record = (ImageView) findViewById(R.id.record);
        stop = (ImageView) findViewById(R.id.stop);
        done = (ImageView) findViewById(R.id.done);

        // Create the MediaPlayer
        mMediaPlayer = MediaPlayer.create(this, R.raw.a);
        Log.d(TAG,
                "MediaPlayer audio session ID: "
                        + mMediaPlayer.getAudioSessionId());



        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        setVolumeControlStream(AudioManager.STREAM_SYSTEM);

                    }
                });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        handler.post(updateThread);

        play.setOnClickListener(this);
        play.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && mMediaPlayer != null) {
            handler.removeCallbacks(updateThread);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
            isListen = true;
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(filePath));

            mMediaPlayer.start();
            minute = 0;
            second = 0;
            final long start = System.currentTimeMillis();
            final Handler handler = new Handler();
            handler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (isListen) {
                                timerHandlerPlay();
                                if (System.currentTimeMillis() - start >= (currentDuration - 1) * 1000) {
                                    stopListen();
                                }
                                handler.postDelayed(this, 1000);
                            }
                        }
                    });



        } else if(v.getId() == R.id.record){
            record.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            start();
        } else if (v.getId() == R.id.stop){
            stop.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            stopRecording();
        } else if (v.getId() == R.id.done){
            done();
        }
    }

    public void start() {
        // startChange();
        final long start = System.currentTimeMillis();
        final Handler handler = new Handler();
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setOutputFile(filePath);
        myAudioRecorder.setMaxDuration(maxDuration);

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (isContinue) {
                            timerHandler();
                            if (System.currentTimeMillis() - start >= maxDuration) {
                                stopRecording();
                            }
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
    }

    private void stopListen() {
        isListen = false;
        //	stopChange();
        mMediaPlayer.stop();
        mMediaPlayer.release();

    }

    private void stopRecording() {
        isContinue = false;
        //	stopChange();
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;

    }

    private void done() {
        Intent intent = new Intent(AudioRecordingActivity.this, AudioStatus.class);
        intent.putExtra("path", filePath);
        intent.putExtra("audioTime", time);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void timerHandler() {
        if (second == 59) {
            second = 0;
            minute++;
        } else {
            second++;
        }
        currentDuration++;
        time = String.format("%02d : %02d", minute, second);
        timer.setText(time);
    }

    private void timerHandlerPlay() {
        if (second == 59) {
            second = 0;
            minute++;
        } else {
            second++;
        }
        timePlay = String.format("%02d : %02d", minute, second);
        timer.setText(timePlay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
