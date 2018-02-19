package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.security.AccessController.getContext;

/**
 * Created by anjal on 4/16/2017.
 */

public class VerificationMail {

    Context context;
    VerificationMail(Context c)
    {
        this.context=c;
    }
    void sendVerificationMail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(context, "Please check your inbox for the verification email.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
