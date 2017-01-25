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

import in.voiceme.app.voiceme.DiscoverPage.LatestListAdapter;
import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public class ActivityInteractionFragment extends BaseFragment {
    public static final String ARG_INTERACTION_PAGE = "ARG_INTERACTION_PAGE";

    private int mPage;
    private RecyclerView recyclerView;
    private LatestListAdapter activityInteractionAdapter;

    public ActivityInteractionFragment() {
        // Required empty public constructor
    }

    public static ActivityInteractionFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_INTERACTION_PAGE, page);
        ActivityInteractionFragment fragment2 = new ActivityInteractionFragment();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_INTERACTION_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_interaction, container, false);
        try {
            initUiView(view);
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initUiView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_interaction_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public String toString() {
        return "documentary";
    }

    private void getData() throws Exception {
        ((VoicemeApplication) getActivity().getApplication()).getWebService()
                .getActivityPosts(MySharedPreferences.getUserId(preferences),"true", MySharedPreferences.getUserId(preferences))
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
        activityInteractionAdapter = new LatestListAdapter(myList, getActivity());
        activityInteractionAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        });
        recyclerView.setAdapter(activityInteractionAdapter);
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
