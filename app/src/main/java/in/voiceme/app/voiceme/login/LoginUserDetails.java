package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emmasuzuki.easyform.EasyTextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import rx.android.schedulers.AndroidSchedulers;

public class LoginUserDetails extends BaseActivity implements View.OnClickListener {
    private Button button;

    private EasyTextInputLayout username;
    private EasyTextInputLayout about_me;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_details);

        token = FirebaseInstanceId.getInstance().getToken();

        // Log.d("Id Generated", token);
        Toast.makeText(LoginUserDetails.this, token, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("LoginUser Details");
        setNavDrawer(new MainNavDrawer(this));

        username = (EasyTextInputLayout) findViewById(R.id.login_start_username);
        about_me = (EasyTextInputLayout) findViewById(R.id.login_start_about_me);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        button = (Button) findViewById(R.id.submit_user_data_button);
        button.setOnClickListener(this);
    }

    private void submitDataWithoutProfile() throws Exception {
        application.getWebService()
                .LoginUserName(MySharedPreferences.getSocialID(preferences), username.getEditText().getText().toString(),
                        about_me.getEditText().getText().toString(), token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AboutmeResponse>() {
                    @Override
                    public void onNext(AboutmeResponse response) {

                        Toast.makeText(LoginUserDetails.this,
                                "result from update profile " + response.status, Toast.LENGTH_SHORT).show();
                        MySharedPreferences.registerUsername(preferences, username.getEditText().getText().toString());
                        //Todo add network call for uploading profile_image file
                        startActivity(new Intent(LoginUserDetails.this, MainActivity.class));
                    }
                });
    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.submit_user_data_button) {
            if (username.getEditText().getText().toString() != null &&
                    about_me.getEditText().getText().toString() != null){
                try {
                    submitDataWithoutProfile();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } else {
            Toast.makeText(this, "Please Enter Username and About Yourself", Toast.LENGTH_SHORT).show();
        }
    }

//  @Override
//   public boolean processLoggedState(View viewPrm) {
//
//  }
}
