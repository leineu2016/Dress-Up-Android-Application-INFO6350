package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    // NOTE: mAuth is defined in BaseActivity
    private static final String TAG = "LoginActivity";
    EditText editTxtEmail;
    EditText editTxtPassword;
    Button btnLogin;
    Button btnSignUp;
    TextView linkForgotPassword;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTxtEmail = (EditText) findViewById(R.id.input_email);
        editTxtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        linkForgotPassword = (TextView) findViewById(R.id.link_forgot_pass);

        linkForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPswd();
            }

            private void forgotPswd() {
                final AlertDialog.Builder forgotPswd = new AlertDialog.Builder(LoginActivity.this);
                forgotPswd.setTitle("Forgot password");
                forgotPswd.setMessage("Enter your mail address below:");
                final EditText email=new EditText(getApplicationContext());
                email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                forgotPswd.setView(email);
                forgotPswd.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String thisEmail= email.getText().toString();
                        passwordReset(thisEmail);
                        dialog.dismiss();
                    }
                });
                AlertDialog fdialog = forgotPswd.create();
                fdialog.show();
            }
        });
    }

    private void passwordReset(String thisEmail) {
        mAuth.sendPasswordResetEmail(thisEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(),"Please check your mail to complete the reset.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"Error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    public void SignUp(View v) {
        Intent i = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(i);
    }

    public void Login(View v) {
        if (!validate()) {
            return;
        }
        // Block button
        btnLogin.setEnabled(false);
        showProgressDialog();

        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString();

        // Login with Firebase
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast toast = Toast.makeText(getBaseContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    btnLogin.setEnabled(true);
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(!user.isEmailVerified()) {
                        setErrorDialog();
                    }
                    else {
                        onLoginSuccess();
                    }
                }
            }
        });
    }

    private void setErrorDialog() {
        btnLogin.setEnabled(true);
        editTxtPassword.setText("");
        final AlertDialog.Builder emailVerification=new AlertDialog.Builder(LoginActivity.this);
        emailVerification.setTitle("Verify your E-mail address");
        emailVerification.setMessage("Your E-mail address is yet to be verified. Check the inbox of the registered E-mail address or click resend below.");

        emailVerification.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
        });

        emailVerification.setNeutralButton("Resend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               new VerificationMail(getApplicationContext()).sendVerificationMail();
            }
        });
        AlertDialog edialog=emailVerification.create();
        edialog.show();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        // Ken: No need for Toast here, we can show a "Welcome" on the MainActivity instead
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }


    public boolean validate() {
        boolean valid = true;

        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTxtEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            editTxtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            editTxtPassword.setError("Must be at least 6 characters");
            valid = false;
        } else {
            editTxtPassword.setError(null);
        }

        return valid;
    }

    private void showProgressDialog() {
        // Progress
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

}

