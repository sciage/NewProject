package in.voiceme.app.voiceme.ActivityPage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.like.LikeButton;

import java.util.List;

import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public class ActivityYourFeedFragment extends BaseFragment {

    public static final String ARG_YOUR_FEED_PAGE = "ARG_INTERACTION_PAGE";

    private int mPage;
    private RecyclerView recyclerView;
    private ActivityYourFeedAdapter activityYourFeedAdapter;

    public ActivityYourFeedFragment() {
        // Required empty public constructor
    }

    public static ActivityYourFeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_YOUR_FEED_PAGE, page);
        ActivityYourFeedFragment fragment2 = new ActivityYourFeedFragment();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_YOUR_FEED_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_your_feed, container, false);
        try {
            initUiView(view);
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initUiView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_feed_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public String toString() {
        return "documentary";
    }

    private void getData() throws Exception {
        ((VoicemeApplication) getActivity().getApplication()).getWebService()
                .getPopulars(MySharedPreferences.getUserId(preferences),"true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showRecycleWithDataFilled(response);
                    }
                });
    }

    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        activityYourFeedAdapter = new ActivityYourFeedAdapter(myList, getActivity());
        activityYourFeedAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        });
        recyclerView.setAdapter(activityYourFeedAdapter);
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