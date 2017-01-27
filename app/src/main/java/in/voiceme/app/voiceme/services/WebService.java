package in.voiceme.app.voiceme.services;

import android.net.Uri;

import java.util.List;

import in.voiceme.app.voiceme.PostsDetails.UserCommentModel;
import in.voiceme.app.voiceme.PostsDetails.UserSuperList;
import in.voiceme.app.voiceme.ProfilePage.FollowerUserList;
import in.voiceme.app.voiceme.ProfilePage.ProfileUserList;
import in.voiceme.app.voiceme.contactPage.AddContactResponse;
import in.voiceme.app.voiceme.login.AboutmeResponse;
import in.voiceme.app.voiceme.login.LoginResponse;
import in.voiceme.app.voiceme.userpost.BaseResponse;
import in.voiceme.app.voiceme.userpost.Response;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {
    @GET("posts.php")
    Observable<List<PostsModel>> getLatestFeed(@Query("user_id") String userID,
                                               @Query("page") int page);

    // Todo donot know about this call
    @GET("posts.php")
    Observable<List<PostsModel>> getFollowers(@Query("user_id") String userID,
                                              @Query("follower") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getPopulars(@Query("user_id") String userID,
                                             @Query("popular") String booleann);

    @GET("posts.php")
    Observable<List<PostsModel>> getTrending(@Query("user_id") String userID,
                                             @Query("trending") String booleann);

    @GET("posts.php")
    Observable<List<PostsModel>> getSinglePost(@Query("id_posts") String booleann,
                                               @Query("user_id") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getSingleUserPosts(@Query("id_user") String id_user,
                                                    @Query("user_id") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getUserFollowerPost(@Query("follower") String follower,
                                                     @Query("user_id") String user_id,
                                                     @Query("page") int page);

    @GET("posts.php")
    Observable<List<PostsModel>> getActivityPosts(@Query("id_user") String id_user,
                                                  @Query("filtered") String filtered,
                                                  @Query("user_id") String user_id);

    @GET("posts.php")
    Observable<List<PostsModel>> getContactPosts(@Query("id_user_name") String id_user_name,
                                                  @Query("user_id") String user_id,
                                                  @Query("contacts") String contacts);

    @GET("get_comments.php")
    Observable<List<UserCommentModel>> getUserComments(
            @Query("id_posts") String id_posts);



    @GET("posts.php")
    Observable<List<PostsModel>> getEmotionPosts(@Query("feeling_id") String feeling_id,
                                                 @Query("user_id") String userID);

    @GET("posts.php")
    Observable<List<PostsModel>> getCategoryPosts(@Query("category_id") String category_id,
                                                  @Query("user_id") String userID);


    @FormUrlEncoded
    @POST("likes.php")
    Observable<LikesResponse> likes(@Field("user_id") String userId,
                                    @Field("post_id") String postId,
                                    @Field("like") int like,
                                    @Field("hug") int hug,
                                    @Field("same") int same,
                                    @Field("listen") int listen);

    @FormUrlEncoded
    @POST("aboutme.php")
    Observable<AboutmeResponse> LoginUserName(@Field("user_id") String userId,
                                              @Field("username") String username,
                                              @Field("about_me") String about_me,
                                              @Field("token") String token);


    @FormUrlEncoded
    @POST("unlike.php")
    Observable<LikesResponse> unlikes(@Field("user_id") String userId,
                                      @Field("post_id") String postId,
                                      @Field("like") int like,
                                      @Field("hug") int hug,
                                      @Field("same") int same,
                                      @Field("audio") int listen);

    @FormUrlEncoded
    @POST("login_new.php")
    Observable<LoginResponse> login(
            @Field("name") String name,
            @Field("email") String email,
            @Field("location") String location,
            @Field("dob") String dateOfBirth,
            @Field("user_id") String userId,
            @Field("profile") Uri profile,
            @Field("gender") String gender,
            @Field("about_me") String aboutme,
            @Field("user_nick_name") String userNickName

    );

    @FormUrlEncoded
    @POST("login_new.php")
    Observable<LoginResponse> loginWithoutProfile(
            @Field("name") String name,
            @Field("email") String email,
            @Field("location") String location,
            @Field("dob") String dateOfBirth,
            @Field("user_id") String userId,
            @Field("gender") String gender,
            @Field("about_me") String aboutme,
            @Field("user_nick_name") String userNickName

    );

    @FormUrlEncoded
    @POST("postStatus.php")
    Observable<Response> postStatus(
            @Field("user_id") String user_id,
            @Field("post_text") String postStatus,
            @Field("cat_id") String cat_id,
            @Field("feeling_id") String feeling_id,
            @Field("audio") String audio,
            @Field("audio_duration") String audio_duration
    );



    @GET("get_likers.php")
    Observable<UserSuperList> getInteractionPosts(
            @Query("id_posts") String id_posts);

    @FormUrlEncoded
    @POST("register_mobile.php")
    Observable<BaseResponse> registerMobile(
            @Field("id_user_name") String user_id,
            @Field("phone_number") String phone_number
    );

    // Todo adding all contacts mobile left
    @FormUrlEncoded
    @POST("register_user_contacts.php")
    Observable<AddContactResponse> addAllContacts(
            @Field("id_user_name") String user_id,
            @Field("contacts") String contacts
    );

    @FormUrlEncoded
    @POST("follower.php")
    Observable<Response> addFollower(
            @Field("user_id") String user_id,
            @Field("follower_id") String follower_id,
            @Field("action") String action
    );


    @GET("follower.php")
    Observable<FollowerUserList> getUserFollow(@Query("user_id") String user_id);

    @GET("follower.php")
    Observable<FollowerUserList> getUserFollowing(@Query("follower_id") String feeling_id);

    @GET("get_user.php")
    Observable<ProfileUserList> getUserProfile(
            @Query("user_id") String user_id);

    @GET("get_user.php")
    Observable<ProfileUserList> getOtherUserProfile(
            @Query("user_id") String user_id,
            @Query("follow_id") String follower);




    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST("audio_upload/index.php")
    Observable<String> uploadFile(@Part MultipartBody.Part file);


    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> getResponse(
            @Query("dataMsg") String dataMessage
    );

    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> sendLikeNotification(
            @Query("dataMsg") String dataMessage
    );

    @GET("http://voiceme.us-east-1.elasticbeanstalk.com/fcm")
    Observable<String> sendFollowNotification(
            @Query("dataMsg") String dataMessage
    );

    @FormUrlEncoded
    @POST("postComments.php")
    Observable<Response> sendComment(
            @Field("id_user_name") String user_id,
            @Field("id_posts") String id_posts,
            @Field("message") String message
    );

}
