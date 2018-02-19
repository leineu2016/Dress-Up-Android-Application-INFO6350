package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spinner;
    EditText name, email, pswd, confirm;
    Button signup;

    String thisName,thisGender,thisMail;
    String thisPswd,thisConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.nameET);
        name.setHintTextColor(Color.BLACK);

        email = (EditText) findViewById(R.id.emailET);
        //email.setHintTextColor(Color.BLACK);

        pswd = (EditText) findViewById(R.id.pswdET);
        // pswd.setHintTextColor(Color.BLACK);
        pswd.setTypeface(Typeface.DEFAULT);
        pswd.setTransformationMethod(new PasswordTransformationMethod());

        confirm = (EditText) findViewById(R.id.confirmET);
        // confirm.setHintTextColor(Color.BLACK);
        confirm.setTypeface(Typeface.DEFAULT);
        confirm.setTransformationMethod(new PasswordTransformationMethod());

        spinner = (Spinner) findViewById(R.id.gender);
        spinner.setOnItemSelectedListener(this);

        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("Gender");
        spinnerList.add("Female");
        spinnerList.add("Male");
        spinnerList.add("Other");

        ArrayAdapter<String> gender = new ArrayAdapter<String>(this, R.layout.spinner_layout, spinnerList);
        gender.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(gender);

        signup = (Button) findViewById(R.id.signupBTN);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        thisName=name.getText().toString().trim();
       thisMail =email.getText().toString();
       thisPswd=pswd.getText().toString();
       thisConfirm=confirm.getText().toString();
       if(validateInput()) {
            mAuth.createUserWithEmailAndPassword(thisMail, thisPswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(getBaseContext(), "Error: "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                (new VerificationMail(getApplicationContext())).sendVerificationMail();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
        }
    }

    private boolean validateInput() {
        if(thisName.isEmpty()) {
            name.setError("Name should not be empty");
            return false;
        }
        if(thisMail.isEmpty()){
            name.setError("E-mail id should not be empty");
            return false;
        }

        if(!(thisPswd.equals(thisConfirm))) {
            confirm.setError("Password do not match");
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        thisGender = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
