package in.voiceme.app.voiceme.PostsDetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public class PostsDetailsActivity extends BaseActivity implements View.OnClickListener, OnLikeListener {

    EditText mMessageEditText;
    ImageButton mSendMessageImageButton;
    RecyclerView mMessageRecyclerView;
    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String postId;
    private PostsModel myList;
    private static LikeUnlikeClickListener myClickListener;

    private ImageView user_avatar;
    private ImageView play_button;

    private TextView user_name;
    private TextView isPost;
    private TextView feeling;
    private TextView category;

    //post data
    private TextView timeStamp;
    private TextView postMessage;
    private TextView postReadMore;
    private TextView post_audio_duration;

    //counter numbers
    private TextView like_counter;
    private TextView hug_counter;
    private TextView same_counter;
    private TextView post_comments;
    private TextView post_listen;

    //emoji for like, hug and same above
    private ImageView likeCounterImage;
    private ImageView hugCounterImage;
    private ImageView sameCounterImage;
    private ImageView commentCounterImage;
    private ImageView listenCounterImage;

    //animated buttons
    private LikeButton likeButtonMain, HugButtonMain, SameButtonMain;



    private MessageAdapter.InsertMessageListener mInsertMessageListener = new MessageAdapter.InsertMessageListener() {

        @Override
        public void onMessageInserted(int position) {
            mLinearLayoutManager.scrollToPosition(position);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_details);
        getSupportActionBar().setTitle("Home");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        postId = getIntent().getStringExtra(Constants.POST_BACKGROUND);



        //Imageview for avatar and play pause button
        user_avatar = (ImageView) findViewById(R.id.detail_list_item_posts_avatar);
        play_button = (ImageView) findViewById(R.id.detail_list_item_posts_play_button);

        //username, feeling and category
        user_name = (TextView) findViewById(R.id.detail_list_item_post_userNickName);
        isPost = (TextView) findViewById(R.id.detail_list_item_post_is);
        feeling = (TextView) findViewById(R.id.detail_list_item_posts_feeling);
        category = (TextView) findViewById(R.id.detail_list_item_posts_category);

        //post data
        post_audio_duration = (TextView) findViewById(R.id.detail_list_item_posts_duration_count);
        timeStamp = (TextView) findViewById(R.id.detail_list_item_posts_timeStamp);
        postMessage = (TextView) findViewById(R.id.detail_list_item_posts_message);
        postReadMore = (TextView) findViewById(R.id.detail_list_item_posts_read_more);

        //counter numbers
        like_counter = (TextView) findViewById(R.id.detail_post_likes_counter);
        hug_counter = (TextView) findViewById(R.id.detail_post_hugs_counter);
        same_counter = (TextView) findViewById(R.id.detail_post_same_counter);
        post_comments = (TextView) findViewById(R.id.detail_post_comment_counter);
        post_listen = (TextView) findViewById(R.id.detail_post_listen_counter);

        //emoji for like, hug and same above
        likeCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_like);
        hugCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_hug);
        sameCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_same);
        commentCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_comment);
        listenCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_listen);

        //animated buttons
        likeButtonMain = (LikeButton) findViewById(R.id.detail_list_item_like_button);
        HugButtonMain = (LikeButton) findViewById(R.id.detail_list_item_hug_button);
        SameButtonMain = (LikeButton) findViewById(R.id.detail_list_item_same_button);




        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mSendMessageImageButton = (ImageButton) findViewById(R.id.detail_btn_send_message);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.detail_rv_messages);

        initRecyclerView();

        mSendMessageImageButton.setOnClickListener(this);
        //OnClickListeners
        likeButtonMain.setOnLikeListener(this);
        HugButtonMain.setOnLikeListener(this);
        SameButtonMain.setOnLikeListener(this);

        like_counter.setOnClickListener(this);
        hug_counter.setOnClickListener(this);
        same_counter.setOnClickListener(this);
        post_comments.setOnClickListener(this);
        post_listen.setOnClickListener(this);
        category.setOnClickListener(this);
        feeling.setOnClickListener(this);
        likeCounterImage.setOnClickListener(this);
        hugCounterImage.setOnClickListener(this);
        commentCounterImage.setOnClickListener(this);
        listenCounterImage.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_avatar.setOnClickListener(this);

        try {
            if (postId != null){
                Toast.makeText(this, "post is not null: " + postId, Toast.LENGTH_SHORT).show();
            }
            getData(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerView() {
        mMessageAdapter = new MessageAdapter(PostsDetailsActivity.this, mInsertMessageListener);

        mLinearLayoutManager = new LinearLayoutManager(PostsDetailsActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mLinearLayoutManager.setStackFromEnd(true);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
    }


    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.detail_btn_send_message) {
            sendMessage();
        }
    }

    void sendMessage() {
        String message = mMessageEditText.getText().toString();
        String imageUri = "https://pp.vk.me/c631524/v631524029/453fe/YONZuru763Q.jpg";
        String userName = "@user_name2133";

        if (!TextUtils.isEmpty(message)) {
            mMessageEditText.setText("");
            mMessageAdapter.addMessage(new MessageItem(message, imageUri, userName));
        } else {
            mMessageAdapter.addMessage(new MessageItem("AAA???777??/A", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQmNCk7L76tvylZw_VPB3oYMHUDejqnl4TvgpGhpB4qEJdu1oDh", "SlowPoke"));
        }
    }

    private void getData(String postId) throws Exception {
        application.getWebService()
                .getSinglePost(postId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        Toast.makeText(PostsDetailsActivity.this, "response for details", Toast.LENGTH_SHORT).show();
                        showRecycleWithDataFilled(response);
                    }
                });
    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {

        for(int i = 0; i < myList.size(); i++) {
            if (i==1){
                String text = myList.get(i).getUserNicName();
                user_name.setText(text);
            } else if (i == 2){
                String text = "time";
                timeStamp.setText(text);
            } else if (i == 3){
                String text = String.valueOf(myList.get(i).getTextStatus());
                postMessage.setText(text);
            } else if (i == 8){
                String text = String.valueOf(myList.get(i).getEmotions());
                feeling.setText(text);
            }else if (i == 9){
                String text = String.valueOf(myList.get(i).getCategory());
                category.setText(text);
            }else if (i == 14){
                String text = String.valueOf(myList.get(i).getComments());
                post_comments.setText(text);
            }else if (i == 10){
                String text = String.valueOf(myList.get(i).getLikes());
                like_counter.setText(text);
            }else if (i == 11){
                String text = String.valueOf(myList.get(i).getSame());
                same_counter.setText(text);
            }else if (i == 12){
                String text = String.valueOf(myList.get(i).getHug());
                hug_counter.setText(text);
            }else if (i == 13){
                String text = String.valueOf(myList.get(i).getListen());
                post_listen.setText(text);
            }
        }



        play_button.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
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

    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }
}
