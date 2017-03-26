package appleoctopus.lastword.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.util.Time;

/**
 * Created by lin1000 on 2017/3/19.
 */

@IgnoreExtraProperties
public class User {

    @Exclude
    private final static String TAG = "User";
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
    private List<String> videoKeys;
    private Boolean isEmailVerified;
    private String lastUpdateTime;

    @Exclude
    private List<Video> videoList;

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

    public List<String> getVideoKeys() {
        return videoKeys;
    }

    public void setVideoKeys(List<String> videoKeys) {
        this.videoKeys = videoKeys;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fbId", fbId);
        result.put("fbEmail", fbEmail);
        result.put("fbPhotoUrl", fbPhotoUrl);
        result.put("googleId", googleId);
        result.put("googleEmail", googleEmail);
        result.put("displayName", displayName);
        result.put("phoneNumber", phoneNumber);
        result.put("password", password);
        result.put("isShowIntro", isShowIntro);
        result.put("isImportFriend", isImportFriend);
        result.put("isEmailVerified", isEmailVerified);
        result.put("videos", videoKeys);
        result.put("lastUpdateTime", lastUpdateTime);

        return result;
    }

    @Exclude
    public void fetchVideoList() {

        videoList = new ArrayList<Video>();

        FirebaseDB.getInstance().getReference().child(FirebaseDB.VIDEO).child(fbId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User Object
                Log.d(TAG, "addListenerForSingleValueEvent.ValueEventListener dataSnapshot ");
                for (DataSnapshot videoKeyDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "addListenerForSingleValueEvent.ValueEventListener videoKeyDataSnapshot.getKey() " + videoKeyDataSnapshot.getKey());
                    Video v = videoKeyDataSnapshot.getValue(Video.class);
                    Log.d(TAG, "addListenerForSingleValueEvent.ValueEventListener videokey " + v.getLocalVideoUri());
                    videoList.add(v);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //return videoList;
    }

    @Exclude
    public void autoPopulateWheneverPossible(){

        String timestamp  = Time.getCurrentTimeUTC();

        setShowIntro(false);
        setLastUpdateTime(timestamp);
    }
}
