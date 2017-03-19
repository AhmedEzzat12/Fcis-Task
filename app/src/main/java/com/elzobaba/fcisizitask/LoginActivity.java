package com.elzobaba.fcisizitask;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Lgin Activity";
    private TextInputLayout inputLayoutmail, inputLayoutpassword;
    private ProgressBar progressBar;
    private String FirebaseChildkey;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;
    private TextView forgotpass;
    private FirebaseAuth mAuth;
    private Button login;
    private DatabaseReference users;
    private ValueEventListener valueEvent;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.email_sign_in_button);
        forgotpass = (TextView) findViewById(R.id.forgot_password);
        inputLayoutmail = (TextInputLayout) findViewById(R.id.TextInputLayoutmail);
        inputLayoutpassword = (TextInputLayout) findViewById(R.id.TextInputLayoutpass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // saveCurrentDataToFB_DB(email.getText().toString().toLowerCase());
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable())
                    SignIn();
                else
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_LONG).show();

            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable())
                    startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                else
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_LONG).show();

            }

        });
    }

//    private void saveCurrentDataToFB_DB(String s) {
//
//        String[] fields = s.split("\\.");
//        final String key = keyGenerator(fields);
//        users = database.getReference("users");
//        valueEvent = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserModel userModel = dataSnapshot.child(String.valueOf(key)).getValue(UserModel.class);
//                Log.d("onDataChange", userModel.getEmail());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        users.addValueEventListener(valueEvent);
//    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void SignIn() {
        final String emailtext = email.getText().toString();
        final String passtext = password.getText().toString();

        if (TextUtils.isEmpty(emailtext)) {
            inputLayoutmail.setError("email cannot be blank");
            return;
        } else {
            inputLayoutmail.setErrorEnabled(false);

        }
        if (TextUtils.isEmpty(passtext)) {
            inputLayoutpassword.setError("password cannot be blank");
            return;
        } else {
            inputLayoutpassword.setErrorEnabled(false);
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailtext, passtext)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            if (passtext.length() < 6) {

                                inputLayoutpassword.setError("Minim 6 characters required");

                            } else {
                                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                            }

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);

                            SaveSharedPreference.setUserName(getApplicationContext(), emailtext.toLowerCase());
                        }

                        // ...
                    }
                });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if (users != null)
        //users.removeEventListener(valueEvent);

    }
}

