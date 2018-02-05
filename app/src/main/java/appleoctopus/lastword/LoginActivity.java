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

import appleoctopus.lastword.firebase.FirebaseDB;
import appleoctopus.lastword.models.User;
import appleoctopus.lastword.util.SharePreference;

/**
 * Created by lin1000 on 2017/3/19.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "facebooklogin";

    //firebase auth
    private FirebaseAuth mAuth;

    //FaceBook
    private CallbackManager callbackManager;
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
                    return;
                }

                if(!user.isEmailVerified()) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {if (task.isSuccessful()) {
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
                    //intent = new Intent(getApplicationContext(), IntroActivity.class);
                    intent = new Intent(getApplicationContext(), FirstOpenDynamicViewActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("displayName",user.getDisplayName());
                    intent.putExtra("email",user.getEmail());
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
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Your account has been disabled. Sorry!",
                                    Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            SharePreference.setFirebaseId(LoginActivity.this, null);
                            LoginManager.getInstance().logOut();
                        }
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
}
