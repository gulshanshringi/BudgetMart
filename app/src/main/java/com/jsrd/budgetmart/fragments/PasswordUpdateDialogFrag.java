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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jsrd.budgetmart.R;

public class PasswordUpdateDialogFrag extends DialogFragment {


    private static final String TAG = "PasswordUpdateDialogFrag";
    private FirebaseUser user;
    private TextInputLayout currentPasswordTxt , newPasswordTxt;
    private Button updateBtn;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_password_update,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentPasswordTxt = getView().findViewById(R.id.currentPassword_PasswordUpdateDialog);
        newPasswordTxt = getView().findViewById(R.id.newPassword_PasswordUpdateDialog);
        updateBtn = getView().findViewById(R.id.updatePassword_PasswordUpdateDialogFrag);
        progressBar = getView().findViewById(R.id.progressBar_PasswordUpdateDialogFrag);
        user = FirebaseAuth.getInstance().getCurrentUser();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }


    private void updatePassword(){

        String currentPassword = currentPasswordTxt.getEditText().getText().toString();

        if (currentPassword.length() >= 8) {
            progressBar.setVisibility(View.VISIBLE);
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                String newPassword = newPasswordTxt.getEditText().getText().toString();

                                if (newPassword.length() >= 8) {
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        getDialog().dismiss();
                                                    }
                                                }
                                            });
                                }else {
                                    Toast.makeText(getContext(), "Please enter valid new Password", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getContext(), "Current Password is not valid", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

        }else {
            Toast.makeText(getContext(), "Please enter valid Current Password", Toast.LENGTH_SHORT).show();
        }

    }


}
