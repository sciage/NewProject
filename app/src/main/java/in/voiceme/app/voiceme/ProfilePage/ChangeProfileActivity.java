package in.voiceme.app.voiceme.ProfilePage;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ChangeProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_SELECT_IMAGE = 100;

    private EditText username;
    private EditText aboutme;
    private EditText userAge;
    private EditText userGender;
    private EditText userLocation;

    private ImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile; //storing the image temporarily while we crop it.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        getSupportActionBar().setTitle("Home");
        setNavDrawer(new MainNavDrawer(this));

        username = (EditText) findViewById(R.id.edittext_profile_username);
        aboutme = (EditText) findViewById(R.id.edittext_profile_aboutme);
        userAge = (EditText) findViewById(R.id.edittext_profile_age);
        userGender = (EditText) findViewById(R.id.edittext_profile_gender);
        userLocation = (EditText) findViewById(R.id.edittext_profile_location);

        avatarView = (ImageView) findViewById(R.id.changeimage);
        avatarProgressFrame = findViewById(R.id.activity_profilechange_avatarProgressFrame);
        tempOutputFile = new File(getExternalCacheDir(), "temp-image.jpg");

        avatarView.setOnClickListener(this);

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        avatarProgressFrame.setVisibility(View.GONE);

    }

    private void changeAvatar() {
        List<Intent> otherImageCaptureIntent = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities = getPackageManager()
                .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0); // finding all intents in apps which can handle capture image
        // loop through all these intents and for each of these activities we need to store an intent
        for (ResolveInfo info : otherImageCaptureActivities) { // Resolve info represents an activity on the system that does our work
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name); // declaring explicitly the class where we will go
            // where the picture activity dump the image
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntent.add(captureIntent);
        }

        // above code is only for taking picture and letting it go through another app for cropping before setting to imageview
        // now below is for choosing the image from device

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Choose Avatar");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                otherImageCaptureIntent.toArray(new Parcelable[otherImageCaptureActivities.size()]));  // add 2nd para as intent of parcelables.

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            tempOutputFile.delete();
            return;
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGE) {
                // user selected an image off their device. other condition they took the image and that image is in our tempoutput file
                Uri outputFile;
                Uri tempFileUri = Uri.fromFile(tempOutputFile);
                // if statement will detect if the user selected an image from the device or took an image
                if (data != null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE))) {
                    //then it means user selected an image off the device
                    // so we can get the Uri of that image using data.getData
                    outputFile = data.getData();
                    // Now we need to do the crop
                } else {
                    // image was out temp file. user took an image using camera
                    outputFile = tempFileUri;
                    // Now we need to do the crop
                }
                startCropActivity(outputFile);

            } else if (requestCode == UCrop.REQUEST_CROP) {
                avatarView.setImageResource(0);

                avatarView.setImageURI(Uri.fromFile(tempOutputFile));


                Timber.e(String.valueOf(Uri.fromFile(tempOutputFile)));
                Toast.makeText(this, String.valueOf(Uri.fromFile(tempOutputFile)), Toast.LENGTH_SHORT).show();

                // avatarProgressFrame.setVisibility(View.VISIBLE);
                // bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
            }

        }
    }

    private void startCropActivity(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempOutputFile));
        uCrop.start(ChangeProfileActivity.this);
    }

    @Override
    public void onClick(View view) {
        processLoggedState(view);
        int viewId = view.getId();

        if (viewId == R.id.changeimage) {
            changeAvatar();
        }
    }

    private void getData() throws Exception {
        application.getWebService()
                .getUserProfile(MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        Timber.e("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        profileData(response);
                    }
                });
    }


    private void profileData(ProfileUserList response) {
        username.setText(response.getData().getUserNickName());
        aboutme.setText(response.getData().getAboutMe());
        userAge.setText(response.getData().getUserDateOfBirth());
        userGender.setText(response.getData().getGender());
        userLocation.setText(response.getData().getLocation());
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
