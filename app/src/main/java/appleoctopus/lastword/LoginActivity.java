package appleoctopus.lastword;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.auth.UserInfo;

import appleoctopus.lastword.Introduction.IntroActivity;
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

    //Custom Image
    private ImageView ivFbCustomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    return;
                }

                // User is signed in , proactily update user information everytime user signed_in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getProviderId());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getPhotoUrl());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.isEmailVerified());
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getToken(false).getResult().getToken());

                // If the above were null, iterate the provider data
                // and set with the first non null data

                //Just leave here for backup and monitor whether disaplay name and photourl will be different from directly getting from user object
                for (UserInfo userInfo : user.getProviderData()) {
                    if (userInfo.getDisplayName() != null) {
                        Log.d(TAG, "onAuthStateChanged:signed_in: userInfo.getDisplayName()" + userInfo.getDisplayName());
                    }
                    if (userInfo.getPhotoUrl() != null) {
                        Log.d(TAG, "onAuthStateChanged:signed_in: userInfo.getPhotoUrl()" + userInfo.getPhotoUrl());
                    }
                }

                if(!user.isEmailVerified()) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {if (task.isSuccessful()) {
                                            Log.d(TAG, "Verification Email sent.");
                                    }}
                                });
                }

                // keep to local storage
                SharePreference.setFirebaseId(LoginActivity.this, user.getUid());

                // Write a message to the database
                User u = new User();
                u.setFbId(user.getUid());
                u.setDisplayName(user.getDisplayName());
                u.setFbEmail(user.getEmail());
                u.setFbPhotoUrl(user.getPhotoUrl().toString());
                u.setEmailVerified(user.isEmailVerified());
                u.fetchVideoList();

                FirebaseDB.getInstance().saveNewUser(u);

                Intent intent = null;
                if (SharePreference.getIsFirstOpen(LoginActivity.this)) {
                    intent = new Intent(getApplicationContext(), IntroActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("displayName",user.getDisplayName());
                    intent.putExtra("email",user.getEmail());
                    SharePreference.setIsFirstOpen(LoginActivity.this, false);
                } else {
                    intent = new Intent(getApplicationContext(), AfterSelfRecordActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        // ...

        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);
        loginButton.setText("");
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
        loginButton.setVisibility(View.GONE);

        ivFbCustomButton = (ImageView)  findViewById(R.id.iv_fb_custom_button);
        ivFbCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
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

        /**
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item_catagory selection
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
