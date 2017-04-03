package appleoctopus.lastword.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import appleoctopus.lastword.models.User;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.util.Time;

/**
 * Created by lin1000 on 2017/3/25.
 */

public class FirebaseDB {

    private static final String TAG = "FirebaseDB";

    //singlton
    private static FirebaseDB instance;

    //Firebase
    private FirebaseDatabase database;

    //JSON Tree Model Path
    public static final String USER = "users";
    public static final String VIDEO = "videos";

    private FirebaseDB(){
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public static FirebaseDB getInstance(){
        if (instance == null) {
            synchronized (FirebaseDB.class) {
                if (instance ==null){
                    instance =  new FirebaseDB();
                }
            }
        }

        return instance;
    }

    public DatabaseReference getReference(String path){
        return database.getReference(path);
    }

    public DatabaseReference getReference(){
        return database.getReference();
    }

    public void setValueOnReference(String path, String value){
        getReference(path).setValue(value);
    }

    public void saveNewUser(User user){

        //auto fields population
        user.autoPopulateWheneverPossible();

        Map<String,Object> userMap = user.toMap();
        getReference().child(USER).child(user.getFbId()).updateChildren(userMap);
    }

    public String saveNewVideo(Video video, String userFbId){
        String key = getReference().child(VIDEO).child(userFbId).push().getKey();

        //auto fields population
        video.autoPopulateWheneverPossible();

        //Using this atomic update approach in case we will need to duplicate this key in different place of json tree
        video.setLastUpdateTime(Time.getCurrentTimeUTC());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + VIDEO +"/" + userFbId + "/" + key, video);

        //When the childUpdates map has been well prepared, update both places as an atomic operation
        getReference().updateChildren(childUpdates);

        return key;
    }

    public void updateVideo(Video video, String userFbId){
        //auto fields population
        video.autoPopulateWheneverPossible();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + VIDEO +"/" + userFbId + "/" + video.getVideoKey(), video);

        //When the childUpdates map has been well prepared, update both places as an atomic operation
        getReference().updateChildren(childUpdates);
    }

    public void removeVideo(Video video, String userFbId, String videoKey){
        DatabaseReference toBeRemoved = getReference().child(VIDEO).child(userFbId).child(videoKey);
        toBeRemoved.removeValue();
    }


}
