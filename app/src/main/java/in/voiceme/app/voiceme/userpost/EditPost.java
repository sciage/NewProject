package in.voiceme.app.voiceme.userpost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class EditPost extends BaseActivity implements View.OnClickListener {
    private EditText remove_edit_posts;
    private Button remove_audio;
    private String idPosts;
    private String doNothingToAudio = "doNothingToAudio";
    private String id_username;
    private String id_posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");
        setNavDrawer(new MainNavDrawer(this));

        remove_edit_posts = (EditText) findViewById(R.id.remove_edit_posts);
        remove_audio = (Button) findViewById(R.id.remove_audio);

        Intent intent = getIntent();
        id_username = intent.getStringExtra(Constants.IDUSERNAME);
        id_posts = intent.getStringExtra(Constants.IDPOST);


        remove_edit_posts.setOnClickListener(this);
        remove_audio.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.remove_audio){
            doNothingToAudio = null;
        } else  if(view.getId() == R.id.remove_edit_posts){

            if (id_username == MySharedPreferences.getUserId(preferences)){
                Toast.makeText(this, "You cannot edit this post page", Toast.LENGTH_SHORT).show();
            } else {
                if (doNothingToAudio != null){
                    try {
                        application.getWebService().EditPosts(idPosts,
                                remove_edit_posts.getText().toString(), null)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseSubscriber<Response>() {
                                    @Override
                                    public void onNext(Response response) {
                                        Timber.e("Response " + response.getStatus() + "===" + response.getMsg());
                                        if (response.getMsg() == "true") {
                                            Toast.makeText(EditPost.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(EditPost.this, MainActivity.class));
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        application.getWebService().updatePostWithoutAudio(idPosts,
                                remove_edit_posts.getText().toString())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseSubscriber<Response>() {
                                    @Override
                                    public void onNext(Response response) {
                                        Timber.e("Response " + response.getStatus() + "===" + response.getMsg());
                                        if (response.getMsg() == "true") {
                                            Toast.makeText(EditPost.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(EditPost.this, MainActivity.class));
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



        }
    }
}
