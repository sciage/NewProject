package in.voiceme.app.voiceme.ProfilePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;

public class SecondProfile extends BaseActivity implements View.OnClickListener {

    private TextView username;
    private TextView about;
    private TextView total_posts;
    private TextView total_posts_counter;
    private TextView followers;
    private TextView following;

    private TextView followersCount;
    private TextView followingCount;

    private TextView age;
    private TextView gender;
    private TextView location;
    private String profileUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileUserId = getIntent().getStringExtra(Constants.SECOND_PROFILE_ID);
        try {
            getData(profileUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_second_profile);
        setNavDrawer(new MainNavDrawer(this));

        username = (TextView) findViewById(R.id.second_name);
        followers = (TextView) findViewById(R.id.second_profile_followers);
        following = (TextView) findViewById(R.id.second_profile_following);
        about = (TextView) findViewById(R.id.second_about);
        total_posts = (TextView) findViewById(R.id.second_user_profile_textview);
        total_posts_counter = (TextView) findViewById(R.id.second_total_posts_counter);

        followersCount = (TextView) findViewById(R.id.second_action_followers);
        followingCount = (TextView) findViewById(R.id.second_action_following);

        age = (TextView) findViewById(R.id.second_age_value);
        gender = (TextView) findViewById(R.id.second_gender_value);
        location = (TextView) findViewById(R.id.second_location_value);

        followers.setOnClickListener(this);
        following.setOnClickListener(this);
        followersCount.setOnClickListener(this);
        followingCount.setOnClickListener(this);

        age.setOnClickListener(this);
        gender.setOnClickListener(this);
        location.setOnClickListener(this);
        total_posts.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);


        //   if (isProgressBarVisible)
        //     setProgressBarVisible(true);

    }

    @Override
    public void onClick(View view) {
        if (processLoggedState(view))
            return;
        int viewId = view.getId();

        if (viewId == R.id.second_user_profile_textview) {
            startActivity(new Intent(this, TotalPostsActivity.class));
        } else if (viewId == R.id.second_profile_followers || viewId == R.id.second_action_followers) {
            startActivity(new Intent(this, FollowersActivity.class));
        } else if (viewId == R.id.second_profile_following || viewId == R.id.second_action_following) {
            startActivity(new Intent(this, FollowingActivity.class));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        l.a(2222);
        if (itemId == R.id.activity_profile_menuEdit) {
            return true;
        } else if (itemId == R.id.activity_profile_menuChangePassword) {
            //  startActivity(new Intent(this, AppConfigsActivity.class));
            return true;
        }

        return false;
    }

    private void getData(String secondUserId) throws Exception {
        application.getWebService()
                .getOtherUserProfile(secondUserId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        secondProfileData(response);
                    }
                });
    }

    private void secondProfileData(ProfileUserList response) {
        username.setText(response.getData().getUserNickName());
        about.setText(response.getData().getAboutMe());
        total_posts_counter.setText(response.getData().getPosts());
        followersCount.setText(response.getData().getFollowers());
        followingCount.setText(response.getData().getFollowing());
        age.setText(response.getData().getUserDateOfBirth());
        gender.setText(response.getData().getGender());
        location.setText(response.getData().getLocation());
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }

}
