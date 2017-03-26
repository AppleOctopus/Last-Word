package appleoctopus.lastword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.Video;
import appleoctopus.lastword.util.SharePreference;

import static appleoctopus.lastword.firebase.FirebaseDB.VIDEO;

public class DetailActivity extends AppCompatActivity {
    private static String TAG = DetailActivity.class.getName();
    static String CATEGORY_KEY = "CATEGORY";

    private RecyclerView mRecyclerView;
    private DetailRecyclerViewAdapter mAdapter;
    private ArrayList<Video> videos = new ArrayList();

    private int catogory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        catogory = getIntent().getIntExtra(CATEGORY_KEY, -1);
        if (catogory == -1) {
            Toast.makeText(this, "找不到此類別", Toast.LENGTH_SHORT).show();
            finish();
        }

        int numberInRow = 4;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberInRow));
        mAdapter = new DetailRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Video v = dataSnapshot.getValue(Video.class);
                if (v != null) {
                     if (v.getCategory() == catogory) {
                        videos.add(v);
                    }
                    mAdapter.updateData(videos);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Video v = dataSnapshot.getValue(Video.class);
                if (v != null) {
                    v.getLocalVideoUri();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ValueEventListener valueEventListener = new ValueEventListener() {
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

        FirebaseDB.getInstance()
                .getReference(VIDEO)
                .child(SharePreference.getFirebaseId(this))
                .addChildEventListener(childEventListener);
     }
}
