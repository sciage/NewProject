package in.voiceme.app.voiceme.ProfilePage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import in.voiceme.app.voiceme.PostsDetails.Person;
import in.voiceme.app.voiceme.PostsDetails.RVAdapter;
import in.voiceme.app.voiceme.PostsDetails.UserSuperList;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;

public class FollowingActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private List<Person> persons;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        getSupportActionBar().setTitle("Hugs LoginUser");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv = (RecyclerView) findViewById(R.id.user_following_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        //Todo work on followers user ID
        application.getWebService()
                .getInteractionPosts("1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserSuperList>() {
                    @Override
                    public void onNext(UserSuperList response) {
                        showRecycleWithDataFilled(response);
                    }
                });
    }

    private void showRecycleWithDataFilled(final UserSuperList myList) {
        RVAdapter adapter = new RVAdapter(myList.getLikes());
        rv.setAdapter(adapter);
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
