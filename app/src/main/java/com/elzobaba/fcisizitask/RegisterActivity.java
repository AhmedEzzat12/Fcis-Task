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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutmail, inputLayoutpassword, inputLayoutUsername;
    private EditText name, email, Password, confirmpass;
    private Spinner spinner;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private UserModel userModel;
    private DatabaseReference users;
    private ProgressBar progressBar;

    public static String keyGenerator(String[] s) {
        String key = "fcis";
        for (String value : s) {
            key += value;
        }
        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        name = (EditText) findViewById(R.id.signupusername);
        email = (EditText) findViewById(R.id.signupemailid);
        Password = (EditText) findViewById(R.id.signuppassid);
        confirmpass = (EditText) findViewById(R.id.signupconfirmpassid);
        spinner = (Spinner) findViewById(R.id.spinner);
        Button register = (Button) findViewById(R.id.signupbtnid);
        inputLayoutmail = (TextInputLayout) findViewById(R.id.TextInputLayoutEmail);
        inputLayoutpassword = (TextInputLayout) findViewById(R.id.TextInputLayoutPass);
        inputLayoutUsername = (TextInputLayout) findViewById(R.id.TextInputLayoutUsername);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        users = database.getReference("users");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Password.getText().toString().equals(confirmpass.getText().toString())) {
                    if (isNetworkAvailable())
                        signUp();
                    else
                        Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signUp() {

        String mail = email.getText().toString();
        String pass = Password.getText().toString();
        final String username = name.getText().toString();

        if (TextUtils.isEmpty(username)) {
            inputLayoutUsername.setError("username cannot be blank");
            return;
        } else {
            inputLayoutUsername.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(mail)) {
            inputLayoutmail.setError("email cannot be blank");
            return;
        } else {
            inputLayoutmail.setErrorEnabled(false);

        }

        if (TextUtils.isEmpty(pass)) {
            inputLayoutUsername.setError("password cannot be blank");
            return;
        } else {
            inputLayoutUsername.setErrorEnabled(false);
        }
        if (pass.length() < 6) {
            inputLayoutpassword.setError("Password too short, enter minimum 6 characters!");
            return;
        } else {
            inputLayoutpassword.setErrorEnabled(false);

        }
        if (TextUtils.isEmpty(spinner.getSelectedItem().toString())) {
            Toast.makeText(getApplicationContext(), "Please choose a year", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        writeNewUser(name.getText().toString(), email.getText().toString().toLowerCase(), spinner.getSelectedItem().toString().toLowerCase()
                        );
                        SaveSharedPreference.setUserName(getApplicationContext(), email.getText().toString().toLowerCase());
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    } else {
                        // if email already registerd
                        if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                            Toast.makeText(getApplicationContext(), "The email address is already exist", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }


    }

    private void writeNewUser(String name_text, String email_text, String year_text) {

        userModel = new UserModel(name_text, email_text, year_text);
        String[] fields = email_text.split("\\.");
        UserModel.KEY = keyGenerator(fields);
        users.child(UserModel.KEY).setValue(userModel);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
