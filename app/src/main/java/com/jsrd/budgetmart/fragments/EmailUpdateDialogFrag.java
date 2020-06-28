package com.jsrd.budgetmart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jsrd.budgetmart.R;

public class EmailUpdateDialogFrag extends DialogFragment {

    private static final String TAG = "EmailUpdateDialogFrag";
    private FirebaseUser user;
    private TextInputLayout currentEmailTxt , newEmailTxt;
    private Button updateBtn;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.email_update_dialog, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentEmailTxt = getView().findViewById(R.id.currentEmail_EmailUpdateDialog);
        newEmailTxt = getView().findViewById(R.id.newEmail_EmailUpdateDialog);
        updateBtn = getView().findViewById(R.id.updateEmail_EmailUpdateDialogFrag);
        progressBar = getView().findViewById(R.id.progressBar_EmailUpdateDialogFrag);

        user = FirebaseAuth.getInstance().getCurrentUser();

        currentEmailTxt.getEditText().setText(user.getEmail());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmailId();
            }
        });
    }


    private void updateEmailId(){
        String newEmail = newEmailTxt.getEditText().getText().toString().trim();
        if (newEmail.length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Email ID succesfully updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getDialog().dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Please Enter a Valid Email id" , Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getContext(), "Please Enter Email id", Toast.LENGTH_SHORT).show();
        }

    }

}
