package com.example.hazem.facebooklogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hazem.facebooklogin.signup.SignupActivity;
import com.example.hazem.facebooklogin.views.HomeScreen;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button signup,mainlogin;
    EditText loginEmail,loginPassword;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.mainsignup);
        mainlogin = findViewById(R.id.mainlogin);
        loginEmail = findViewById(R.id.loginemail);
        loginPassword = findViewById(R.id.loginpassword);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


        if(pref.getBoolean("logged_in",false)){
            Intent home = new Intent(MainActivity.this,HomeScreen.class);
            startActivity(home);
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.hazem.facebooklogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        callbackManager = CallbackManager.Factory.create();

        //firebase


        // ...
        // Initialize Firebase Auth
        //FirebaseApp.initializeApp(MainActivity.this);

        mAuth = FirebaseAuth.getInstance();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        //

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(MainActivity.this,"login successuful",Toast.LENGTH_SHORT).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this,"login cancelled",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this,"login failled",Toast.LENGTH_SHORT).show();
                    }
                });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });


        mainlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        loginPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    editor.putBoolean("logged_in", true);
                                    editor.putString("user_id",user.getUid());
                                    //editor.putString("user_name",user.getDisplayName());
                                    editor.putString("user_email",user.getEmail());
                                    //editor.putString("user_photo_uri","");
                                    editor.commit();

                                    userRef = FirebaseDatabase.getInstance().getReference(user.getUid() + "11");
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ArrayList<String> arr = (ArrayList<String>) dataSnapshot.getValue();
                                            editor.putString("user_name",arr.get(0));
                                            editor.putString("user_photo_uri",arr.get(1));
                                            editor.commit();
                                            Log.i("ondatachange","user name: " + arr.get(0) + "uri : " + arr.get(1));
                                            Intent home = new Intent(MainActivity.this,HomeScreen.class);
                                            startActivity(home);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }
                        });


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(MainActivity.this,"login result code:  "+ resultCode,Toast.LENGTH_SHORT).show();

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("handlefacebooktokin", "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("handlefacebooktokin", "welcome: "+user.getDisplayName());
                            //save shared prefrences

                            editor.putBoolean("logged_in", true);
                            editor.putString("user_id",user.getUid());
                            editor.putString("user_name",user.getDisplayName());
                            editor.putString("user_email",user.getEmail());
                            editor.putString("user_photo_uri",user.getPhotoUrl().toString());
                            editor.putString("user_credential",credential.toString());
                            editor.commit();

                            Intent home = new Intent(MainActivity.this,HomeScreen.class);
                            //profileactivity.putExtra("facebook",true);
                            startActivity(home);
                            finish();
                            //updateUI(user);
                            Toast.makeText(MainActivity.this,"welcome: "+user.getDisplayName(),Toast.LENGTH_SHORT);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("handlefacebooktokin", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
