package in.voiceme.app.voiceme.contactPage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.userpost.BaseResponse;
import io.fabric.sdk.android.Fabric;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.YES;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TWITTER_KEY = "I6Zt8s6wSZzMtnPqun18Raw0T";
    private static final String TWITTER_SECRET = "Jb92MdEm2GmK40RMqZvoxmjTFR4aUipanCOYr3BHloy43cvOsA";
    private Button agreeTerms;
    private DigitsAuthButton digitsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setTitle("Home");
        setNavDrawer(new MainNavDrawer(this));

        agreeTerms = (Button) findViewById(R.id.button_user_agree);

        agreeTerms.setOnClickListener(this);
        // Create a digits button and callback
        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        digitsButton.setText(R.string.digits_contact);
        digitsButton.setCallback(new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Timber.v("DIGITS SUCCESSFUL authentication");
                Timber.v("phone number: " + phoneNumber);

                try {
                    sendContact(phoneNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Timber.d("Oops Digits issue");
            }
        });

        digitsButton.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.button_user_agree) {
            MySharedPreferences.checkContactSent(preferences, YES);
            digitsButton.setVisibility(View.VISIBLE);
        }
    }

    private void sendContact(String phoneNumber) throws Exception {
        application.getWebService()
                .registerMobile(MySharedPreferences.getUserId(preferences), phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        Timber.e("Successfully logged in" + response.getStatus());

                    }
                });
    }


//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
