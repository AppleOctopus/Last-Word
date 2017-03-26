package appleoctopus.lastword.models;

import java.util.List;

/**
 * Created by lin1000 on 2017/3/24.
 */

public class Video {
    public static int CATOGORY_THX = 1;
    public static int CATOGORY_SORRY = 2;
    public static int CATOGORY_MISS = 3;
    public static int CATOGORY_LIFE = 4;

    public Video(){
        // Default constructor required for calls to DataSnapshot.getValue(Video.class)
    }

    private User owner;
    private String password; // a video specific password which could be used to access this video after protected until date
    private int category; // category of this video
    private String isLocalExist; // true is the video is available on local
    private String localVideoUri;// local uri of the video
    private String lsRemoteExist; // true if the remote uri is available
    private String rmoteVideoUri;//remote uri of the video
    private String videoLength; //length of the video
    private List<User> sharedUsers; //list of users could access to this video after protected until date.
    private String videoFormat;// mp4, avi or others
    private String thumbnailId; //reference to id of thumbnail
    private String createdAt; //date of video created
    private String protectedUntil; // date of available for protected access
    private String publicUntil; // date of available for public access
    private String destroyUntil; // date of unavailable for any access
    private String lastUpdateTime; // date of video updated

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

    public String getIsLocalExist() {
        return isLocalExist;
    }

    public void setIsLocalExist(String isLocalExist) {
        this.isLocalExist = isLocalExist;
    }

    public String getLocalVideoUri() {
        return localVideoUri;
    }

    public void setLocalVideoUri(String localVideoUri) {
        this.localVideoUri = localVideoUri;
    }

    public String getLsRemoteExist() {
        return lsRemoteExist;
    }

    public void setLsRemoteExist(String lsRemoteExist) {
        this.lsRemoteExist = lsRemoteExist;
    }

    public String getRmoteVideoUri() {
        return rmoteVideoUri;
    }

    public void setRmoteVideoUri(String rmoteVideoUri) {
        this.rmoteVideoUri = rmoteVideoUri;
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
}
