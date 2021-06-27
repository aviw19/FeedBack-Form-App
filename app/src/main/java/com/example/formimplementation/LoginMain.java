package com.example.formimplementation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class LoginMain extends AppCompatActivity {
    private static final int RC_SIGN_IN =9001 ;
    private static final String TAG ="Login" ;
    private CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    private FirebaseAuth mAuth;
    Button emailsingnin;
    Button signup;
    Button fbsignin;
    EditText emaile;
    EditText passworde;
    ProgressBar progressBar;
    String name;
    String email;
    Uri photoUrl;
    Spinner spinner;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        fbsignin=findViewById(R.id.login_button);
        emailsingnin=findViewById(R.id.Emailsignin);
        emaile=findViewById(R.id.Email);
        passworde=findViewById(R.id.Password);
        progressBar=findViewById(R.id.progressBar);


        emailsingnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emaile.getText().toString();
                String password=passworde.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginMain.this, new OnCompleteListener<AuthResult>() {
                            private static final String TAG ="Email Sign In" ;

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginMain.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                    updateUI((FirebaseUser) null);
                                }

                                // ...
                            }
                        });

            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInClient = GoogleSignIn.getClient(LoginMain.this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });




        signup=findViewById(R.id.Emailsignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginMain.this,SIgnUp.class);
                startActivity(intent);

            }
        });
        fbsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();



                LoginManager.getInstance().logInWithReadPermissions(LoginMain.this, Arrays.asList("public_profile"));

                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Profile profile =Profile.getCurrentProfile();
                                //nextActivity(profile);
                                Toast.makeText(getApplicationContext(),"Loggin",Toast.LENGTH_SHORT).show();
                                name=profile.getName();
                                email=profile.getId();
                                photoUrl=profile.getProfilePictureUri(200,400);
                                accessTokenTracker.startTracking();
                                profileTracker.startTracking();
                                begin();

                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                exception.printStackTrace();
                            }
                        });

                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                    }
                };
                profileTracker =new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                    }
                };

            }
        });



    }

/*    protected void nextActivity(Profile profile)
    {
        if(profile!=null)
        {



        }
    }*/

    // @Override
    /*public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            begin();

        }
        else
        {
            //Toast.makeText("LoginMain.this","User Not found").show();
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void updateUI(GoogleSignInAccount account) {
        if(account!=null)
        {
            account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            name=account.getDisplayName();
            email=account.getEmail();
            photoUrl=account.getPhotoUrl();
            begin();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI((GoogleSignInAccount) null);
        }
    }

    private void begin() {
        Intent intent=new Intent(LoginMain.this,UserProfile.class);
        //intent.putExtra(name,"name");
        //intent.putExtra(email,"email");
        ///intent.putExtra(String.valueOf(photoUrl),"photoUrl");
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putString("email",email);




        // bundle.putString("photoUrl",photoUrl.toString());
        intent.putExtras(bundle);
        startActivity(intent);




    }
}
