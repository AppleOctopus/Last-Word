package appleoctopus.lastword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;

public class ListActivity extends AppCompatActivity {
    private static String TAG = ListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  Video v = dataSnapshot.getValue(Video.class);
                  if (v != null) {
                      v.getLocalVideoUri();
                  }
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDB.getInstance().getReference().addValueEventListener(postListener);

    }
}
