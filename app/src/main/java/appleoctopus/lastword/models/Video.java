package appleoctopus.lastword.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

import appleoctopus.lastword.util.Time;

/**
 * Created by lin1000 on 2017/3/24.
 */

@IgnoreExtraProperties
public class Video {
    public static int CATOGORY_THX = 1;
    public static int CATOGORY_SORRY = 2;
    public static int CATOGORY_MISS = 3;
    public static int CATOGORY_LIFE = 4;

    @Exclude
    private final static String TAG = "Video";

    @Exclude
    private User owner;

    private String password; // a video specific password which could be used to access this video after protected until date
    private int category; // category of this video
    private Boolean isLocalExist; // true is the video is available on local
    private String localVideoUri;// local uri of the video
    private Boolean IsRemoteExist; // true if the remote uri is available
    private String remoteVideoUri;//remote uri of the video
    private String videoLength; //length of the video
    private List<User> sharedUsers; //list of users could access to this video after protected until date.
    private String videoFormat;// mp4, avi or others
    private String thumbnailId; //reference to id of thumbnail
    private String createdAt; //date of video created
    private String protectedUntil; // date of available for protected access
    private String publicUntil; // date of available for public access
    private String destroyUntil; // date of unavailable for any access
    private String lastUpdateTime; // date of video updated

    public Video(){
        // Default constructor required for calls to DataSnapshot.getValue(Video.class)
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Boolean getLocalExist() {
        return isLocalExist;
    }

    public void setLocalExist(Boolean isLocalExist) {
        this.isLocalExist = isLocalExist;
    }

    public String getLocalVideoUri() {
        return localVideoUri;
    }

    public void setLocalVideoUri(String localVideoUri) {
        this.localVideoUri = localVideoUri;
    }

    public Boolean getRemoteExist() {
        return IsRemoteExist;
    }

    public void setRemoteExist(Boolean IsRemoteExist) {
        this.IsRemoteExist = IsRemoteExist;
    }

    public String getRemoteVideoUri() {
        return remoteVideoUri;
    }

    public void setRemoteVideoUri(String remoteVideoUri) {
        this.remoteVideoUri = remoteVideoUri;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public List<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(List<User> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProtectedUntil() {
        return protectedUntil;
    }

    public void setProtectedUntil(String protectedUntil) {
        this.protectedUntil = protectedUntil;
    }

    public String getPublicUntil() {
        return publicUntil;
    }

    public void setPublicUntil(String publicUntil) {
        this.publicUntil = publicUntil;
    }

    public String getDestroyUntil() {
        return destroyUntil;
    }

    public void setDestroyUntil(String destroyUntil) {
        this.destroyUntil = destroyUntil;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Exclude
    public void autoPopulateWheneverPossible(){

        String timestamp  = Time.getCurrentTimeUTC();

        if (getLocalVideoUri() != null && !getLocalVideoUri().equalsIgnoreCase("")){
            setLocalExist(true);
        } else{
            setLocalExist(false);
        }

        if (getRemoteVideoUri() != null && !getRemoteVideoUri().equalsIgnoreCase("")) {
            setRemoteExist(true);
        } else {
            setRemoteExist(false);
        }

        setCreatedAt(timestamp);
        setLastUpdateTime(timestamp);

    }

}
