package appleoctopus.lastword.models;

import java.util.List;

/**
 * Created by lin1000 on 2017/3/19.
 */

public class User {


    private String fbId;
    private String fbEmail;
    private String fbPhotoUrl;
    private String googleId;
    private String googleEmail;
    private String displayName;
    private String phoneNumber;
    private String password; //user
    private Boolean isShowIntro; // ture by default and false after intro completed
    private Boolean isImportFriend; //true by default and false after 1st time friend import
    private List<Video> videos;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getFbEmail() {
        return fbEmail;
    }

    public void setFbEmail(String fbEmail) {
        this.fbEmail = fbEmail;
    }

    public String getFbPhotoUrl() {
        return fbPhotoUrl;
    }

    public void setFbPhotoUrl(String fbPhotoUrl) {
        this.fbPhotoUrl = fbPhotoUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleEmail() {
        return googleEmail;
    }

    public void setGoogleEmail(String googleEmail) {
        this.googleEmail = googleEmail;
    }

    public Boolean getShowIntro() {
        return isShowIntro;
    }

    public void setShowIntro(Boolean showIntro) {
        isShowIntro = showIntro;
    }

    public Boolean getImportFriend() {
        return isImportFriend;
    }

    public void setImportFriend(Boolean importFriend) {
        isImportFriend = importFriend;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void saveUser() {
        //Add YOUR Firebase Reference URL instead of the following URL
//        Firebase myFirebaseRef = new Firebase("https://lastword-6763b.firebaseio.com/ ");
//        myFirebaseRef = myFirebaseRef.child("users").child(getId());
//        myFirebaseRef.setValue(this);
    }
}
