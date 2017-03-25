package appleoctopus.lastword.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appleoctopus.lastword.models.User;
import appleoctopus.lastword.models.Video;

/**
 * Created by lin1000 on 2017/3/25.
 */

public class FirebaseDB {

    //singlton
    private static FirebaseDB instance;

    //Firebase
    private FirebaseDatabase database;

    //JSON Tree Model Path
    public static final String USER = "users";
    public static final String VIDEO = "videos";

    private FirebaseDB(){
        database = FirebaseDatabase.getInstance();
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
        getReference().child(USER).child(user.getFbId()).setValue(user);
    }

    public void saveNewVideo(Video video, String userFbId){
        getReference().child(VIDEO).child(userFbId).setValue(video);
    }




}
