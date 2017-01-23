package in.voiceme.app.voiceme.contactPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.ContactsHelper;
import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TWITTER_KEY = "I6Zt8s6wSZzMtnPqun18Raw0T";
    private static final String TWITTER_SECRET = "Jb92MdEm2GmK40RMqZvoxmjTFR4aUipanCOYr3BHloy43cvOsA";
    private Button agreeTerms;
    private Button getAllContacts;
    private Button enterButton;
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
        getAllContacts = (Button) findViewById(R.id.button_get_all_contacts);
        enterButton = (Button) findViewById(R.id.enter_contacts_main_page);

        enterButton.setOnClickListener(this);
        agreeTerms.setOnClickListener(this);
        getAllContacts.setOnClickListener(this);
        // Create a digits button and callback
        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        digitsButton.setText(R.string.digits_contact);
        digitsButton.setCallback(new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Timber.v("DIGITS SUCCESSFUL authentication");
                Timber.v("phone number: " + phoneNumber);
                application.getAuth().getUser().setPhoneNumber(true);

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
        getAllContacts.setVisibility(View.GONE);
        enterButton.setBackgroundColor(getResources().getColor(R.color.material_red_500));

    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.button_user_agree) {
            digitsButton.setVisibility(View.VISIBLE);
            getAllContacts.setVisibility(View.VISIBLE);
            // Todo add 2 Tick marks to show that the user has cleared them before going inside
        } else if (view.getId() == R.id.button_get_all_contacts){
            readContacts();

        } else if (view.getId() == R.id.enter_contacts_main_page){

        //    if (application.getAuth().getUser().isPhoneNumber() && application.getAuth().getUser().isAllContacts()){
                enterButton.setBackgroundColor(getResources().getColor(R.color.contacts_green_button));
            MySharedPreferences.checkContactSent(preferences, "Sent");
                startActivity(new Intent(this, ContactListActivity.class));
                finish();
                return;
        //    }

        }
    }

    private void readContacts() {
        if (ActivityUtils.isContactsPermission(this)) {
            getContacts();
        }
    }

    private void getContacts() {

        ProgressDialog dialog = new ProgressDialog(ContactsActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Reading contacts..");
        dialog.show();

        Observable.fromCallable(
                () -> ContactsHelper.readContacts(ContactsActivity.this.getContentResolver()))
                .doOnError(throwable -> Timber.d(throwable.getMessage()))
                .onErrorResumeNext(throwable -> {
                    Timber.d(throwable.getMessage());
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    dialog.dismiss();
                    contacts.remove(0);
                    contacts.remove(contacts.size() - 1);
                    try {
                        sendAllContacts(contacts.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // showContactsCompletedDialog(contacts);
                    Timber.d("comma separated contacts array %s", contacts.toString().replace("[", "").replace("]", ""));
                });
    }

    private void sendAllContacts(String contacts) throws Exception {
        application.getWebService()
                .addAllContacts(MySharedPreferences.getUserId(preferences), contacts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddContactResponse>() {
                    @Override
                    public void onNext(AddContactResponse response) {
                        Timber.e("Got user details " + response.getInsertedRows().toString());
                        application.getAuth().getUser().setAllContacts(true);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == getResources().getInteger(R.integer.contacts_request)) {
                getContacts();
            }
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
