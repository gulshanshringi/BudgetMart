package com.jsrd.budgetmart.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private EditText numberTxt, passwordTxt;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        numberTxt = findViewById(R.id.numberEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        checkUserSignIn();

    }

    private void checkUserSignIn() {
        if (user != null) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }else {phoneNumberAuthentication();}
    }

    public void phoneNumberAuthentication() {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();

                // Toast.makeText(PhoneAuthActivity.this, "Succesful"+phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();
                //signInWithPhoneAuthCredintial(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.i("Fail authentication ", e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                Token = forceResendingToken;
            }
        };


        String userNumber = numberTxt.getText().toString().trim();

        if (userNumber != null) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    userNumber,        // Phone number to verify
                    15,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks


        }

    }

    public void verifyPhoneNumberWithCode(View v) {

        String code = passwordTxt.getText().toString().trim();

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                user = mAuth.getInstance().getCurrentUser();
                if (user != null){
                    FirestoreFirebase ff = new FirestoreFirebase(PhoneAuthActivity.this);
                    ff.putUserInfoOnFirestore(user);
                }
                checkUserSignIn();
            }
        });
    }

}
