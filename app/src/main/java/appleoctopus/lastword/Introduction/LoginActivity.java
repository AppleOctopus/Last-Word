package appleoctopus.lastword.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import appleoctopus.lastword.R;
import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.User;
import appleoctopus.lastword.util.SharePreference;

/**
 * Created by lin1000 on 2017/3/19.
 */

public class LoginActivity extends AppCompatActivity {

    //firebase auth
    private static final String TAG = "facebooklogin";
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    //FaceBook
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getProviderId());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getPhotoUrl());

                    // keep to local storage
                    SharePreference.setFirebaseId(LoginActivity.this, user.getUid());

                    // Write a message to the database
                    User u = new User();
                    u.setFbId(user.getUid());
                    u.setDisplayName(user.getDisplayName());
                    u.setFbEmail(user.getEmail());
                    u.setFbPhotoUrl(user.getPhotoUrl().toString());
                    //user.getProviderData();

                    FirebaseDB.getInstance().saveNewUser(u);

                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("displayName",user.getDisplayName());
                    intent.putExtra("email",user.getEmail());
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("DEBUG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("DEBUG", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("DEBUG", "facebook:onError", error);
                // ...
            }
        });

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Your account has been disabled. Sorry!",
                                    Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            SharePreference.setFirebaseId(LoginActivity.this, null);
                            LoginManager.getInstance().logOut();
                        }

                        // ...
                    }
                });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        //email = (EditText) findViewById(R.id.edit_text_email_id);
        //password = (EditText) findViewById(R.id.edit_text_password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_login);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }


    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //

}
