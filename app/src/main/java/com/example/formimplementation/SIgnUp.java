package com.example.formimplementation;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SIgnUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button signupb;
    EditText emaile;
    EditText passworde;
    EditText name;
    EditText cpassworde;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name=findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        emaile=findViewById(R.id.email);
        passworde=findViewById(R.id.password);
        cpassworde=findViewById(R.id.confirmpassword);




        signupb=findViewById(R.id.signup);
        signupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emaile.getText().toString();
                String password=passworde.getText().toString();
                String confirmpassword=cpassworde.getText().toString();
                if(password.compareTo(confirmpassword)!=0)
                {
                    cpassworde.setError("Password and Confirm Password are not same");
                    cpassworde.requestFocus();
                }


                final String nam=name.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SIgnUp.this, new OnCompleteListener<AuthResult>() {
                            private static final String TAG ="Sign up here" ;

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nam)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SIgnUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        passworde.setError(getString(R.string.error_weak_password));
                                        passworde.requestFocus();
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        emaile.setError(getString(R.string.error_invalid_email));
                                        emaile.requestFocus();
                                    } catch(FirebaseAuthUserCollisionException e) {
                                        emaile.setError(getString(R.string.error_user_exists));
                                        emaile.requestFocus();
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }

                                    updateUI(null);
                                }

                                // ...
                            }

                            private void updateUI(FirebaseUser user) {
                                if(user!=null)
                                {
                                    Toast.makeText(SIgnUp.this, "New User Created",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(SIgnUp.this,LoginMain.class);
                                    startActivity(i);
                                }

                            }
                        });

            }
        });
    }
}
