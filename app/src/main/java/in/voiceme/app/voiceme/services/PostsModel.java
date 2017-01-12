package in.voiceme.app.voiceme.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PostsModel implements Parcelable {
    public static final Creator<PostsModel> CREATOR = new Creator<PostsModel>() {
        @Override
        public PostsModel createFromParcel(Parcel in) {
            return new PostsModel(in);
        }

        @Override
        public PostsModel[] newArray(int size) {
            return new PostsModel[size];
        }
    };
    @SerializedName("id_posts") @Expose private String idPosts;
    @SerializedName("id_user_name") @Expose private String idUserName;
    @SerializedName("post_time") @Expose private String postTime;
    @SerializedName("text_status") @Expose private String textStatus;
    @SerializedName("audio_duration") @Expose private String audioDuration;
    @SerializedName("audio_file_link") @Expose private String audioFileLink;
    @SerializedName("user_nic_name") @Expose private String userNicName;
    @SerializedName("avatar_pics") @Expose private String avatarPics;
    @SerializedName("emotions") @Expose private String emotions;
    @SerializedName("category") @Expose private String category;
    @SerializedName("likes") @Expose private String likes;
    @SerializedName("same") @Expose private String same;
    @SerializedName("hug") @Expose private String hug;
    @SerializedName("listen") @Expose private String listen;
    @SerializedName("comments") @Expose private String comments;
    @SerializedName("user_like") @Expose private Boolean userLike;
    @SerializedName("user_Same") @Expose private Boolean userSame;
    @SerializedName("user_Huge") @Expose private Boolean userHuge;
    @SerializedName("user_Listen") @Expose private Boolean userListen;

    private PostsModel(Parcel in) {
        idPosts = in.readString();
        idUserName = in.readString();
        postTime = in.readString();
        textStatus = in.readString();
        audioDuration = in.readString();
        audioFileLink = in.readString();
        userNicName = in.readString();
        avatarPics = in.readString();
        emotions = in.readString();
        category = in.readString();
        likes = in.readString();
        same = in.readString();
        hug = in.readString();
        listen = in.readString();
        comments = in.readString();
    }

    /**
     * @return The idPosts
     */
    public String getIdPosts() {
        return idPosts;
    }

    /**
     * @return The idUserName
     */
    public String getIdUserName() {
        return idUserName;
    }

    /**
     * @return The postTime
     */
    public String getPostTime() {
        return postTime;
    }

    /**
     * @return The textStatus
     */
    public String getTextStatus() {
        return textStatus;
    }

    /**
     * @return The audioDuration
     */
    public String getAudioDuration() {
        return audioDuration;
    }

    /**
     * @return The audioFileLink
     */
    public String getAudioFileLink() {
        return audioFileLink;
    }

    /**
     * @return The userNicName
     */
    public String getUserNicName() {
        return userNicName;
    }

    /**
     * @return The avatarPics
     */
    public String getAvatarPics() {
        return avatarPics;
    }

    /**
     * @return The emotions
     */
    public String getEmotions() {
        return emotions;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return The likes
     */
    public String getLikes() {
        return likes;
    }

    /**
     * @return The same
     */
    public String getSame() {
        return same;
    }

    /**
     * @return The hug
     */
    public String getHug() {
        return hug;
    }

    /**
     * @return The listen
     */
    public String getListen() {
        return listen;
    }

    /**
     * @return The comments
     */
    public String getComments() {
        return comments;
    }

    public Boolean getUserLike() {
        return userLike;
    }

    public Boolean getUserSame() {
        return userSame;
    }

    public Boolean getUserHuge() {
        return userHuge;
    }

    public Boolean getUserListen() {
        return userListen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idPosts);
        parcel.writeString(idUserName);
        parcel.writeString(postTime);
        parcel.writeString(textStatus);
        parcel.writeString(audioDuration);
        parcel.writeString(audioFileLink);
        parcel.writeString(userNicName);
        parcel.writeString(avatarPics);
        parcel.writeString(emotions);
        parcel.writeString(category);
        parcel.writeString(likes);
        parcel.writeString(same);
        parcel.writeString(hug);
        parcel.writeString(listen);
        parcel.writeString(comments);
    }
}