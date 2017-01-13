package in.voiceme.app.voiceme.userpost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class AudioStatus extends BaseActivity {
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String AUDIO_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + "recorded_audio.wav";
 //   private static final String CONVERTED_AUDIO_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + "recorded_audio.mp3";
    private TextView textView_category;
    private TextView textView_feeling;
    private TextView textView_status;
    private Button post_status;
    private String audioFileUrl;
    private String category;
    private String feeling;
    private String textStatus;
  //  private FFmpeg ffmpeg;
    private ProgressDialog progressDialog;
    private int audioDuration;
  //  private String convertAudioCommand = "-y -i " + AUDIO_FILE_PATH + " -ar 44100 -ac 2 -ab 64k -f mp3 " + CONVERTED_AUDIO_FILE_PATH;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_audio_status);
        getSupportActionBar().setTitle("Audio Status");
        setNavDrawer(new MainNavDrawer(this));

        textView_category = (TextView) findViewById(R.id.textView_category);
        textView_feeling = (TextView) findViewById(R.id.textView_feeling);
        textView_status = (TextView) findViewById(R.id.textView_status);
        post_status = (Button) findViewById(R.id.button_post_audio_status);

     //   ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);

        textView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(AudioStatus.this, CategoryActivity.class);
                startActivityForResult(categoryIntent, 1);
            }
        });
        textView_feeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Intent feelingIntent = new Intent(AudioStatus.this, FeelingActivity.class);
                startActivityForResult(feelingIntent, 2);
            }
        });
        textView_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Intent statusIntent = new Intent(AudioStatus.this, StatusActivity.class);
                startActivityForResult(statusIntent, 3);
            }
        });

        post_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                if (category == null || feeling == null || textStatus == null) {
                    Toast.makeText(AudioStatus.this, "Please select all categories to Post Status", Toast.LENGTH_SHORT).show();
                }
                uploadFile(Uri.parse(AUDIO_FILE_PATH));
                // network call from retrofit

            }
        });

        // loadFFMpegBinary();
    }

  /*  private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        }
    }

    */

   /* private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(AudioStatus.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.device_not_supported))
                .setMessage(getString(R.string.device_not_supported_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AudioStatus.this.finish();
                    }
                })
                .create()
                .show();

    }
    */

   /* private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Toast.makeText(AudioStatus.this, "Failed with Output" + s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    //  Toast.makeText(AudioStatus.this, "Success with Output" + s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(String s) {
                    Timber.d("Started command : ffmpeg " + command);
                    //    Toast.makeText(AudioStatus.this, "progress" + s, Toast.LENGTH_SHORT).show();
                    progressDialog.setMessage("Processing\n" + s);
                }

                @Override
                public void onStart() {

                    Timber.d("Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Timber.d("Finished command : ffmpeg " + command);
                    progressDialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    } */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
                //    Toast.makeText(this, AUDIO_FILE_PATH, Toast.LENGTH_SHORT).show();
                //     Timber.e("file location" + AUDIO_FILE_PATH);

              //  getAudioFileDuration(AUDIO_FILE_PATH);
              //  String[] command = convertAudioCommand.split(" ");
              //  execFFmpegBinary(command);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromCategory");
                category = result;
                Toast.makeText(this, "Data returned: " + result, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromFeeling");
                feeling = result;
                Toast.makeText(this, "Data returned: " + result, Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromStatus");
                textStatus = result;
                Toast.makeText(this, "Data returned: " + result, Toast.LENGTH_SHORT).show();
            }
        }
    }

 /*   private void getAudioFileDuration(String audioFilePath) {
        Uri uri = Uri.parse(audioFilePath);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(AudioStatus.this, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        audioDuration = millSecond;
    } */

    public void recordAudio(View v) {
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(this, R.color.recorder_bg))
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    private void uploadFile(Uri fileUri) {
        File file = new File(String.valueOf(fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // finally, execute the request
        try {
            progressDialog.setMessage("uploading file...");
            progressDialog.show();
            application.getWebService()
                    .uploadFile(body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String response) {
                            Timber.d("file url" + response);
                            Toast.makeText(AudioStatus.this, "file url" + response, Toast.LENGTH_SHORT).show();
                            setAudioFileUrl(response);

                        }

                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();

                            postStatus();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void postStatus() {
        // network call from retrofit
        try {
            application.getWebService().postStatus(MySharedPreferences.getUserId(preferences),
                    textStatus, category, feeling, audioFileUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response>() {
                        @Override
                        public void onNext(Response response) {
                            Timber.e("Response " + response.getStatus() + "===" + response.getMsg());
                            if (response.getStatus() == 1) {
                                startActivity(new Intent(AudioStatus.this, MainActivity.class));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }
}
