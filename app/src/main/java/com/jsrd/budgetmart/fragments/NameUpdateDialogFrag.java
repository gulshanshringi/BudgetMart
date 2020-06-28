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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jsrd.budgetmart.R;

public class NameUpdateDialogFrag extends DialogFragment {


    private static final String TAG = "NameUpdateDialogFrag";
    private FirebaseUser user;
    private TextInputLayout currentNameTxt , newNameTxt;
    private Button updateBtn;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_name_update,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentNameTxt = getView().findViewById(R.id.currentName_NameUpdateDialog);
        newNameTxt = getView().findViewById(R.id.newName_NameUpdateDialog);
        updateBtn = getView().findViewById(R.id.updateName_NameUpdateDialogFrag);
        progressBar = getView().findViewById(R.id.progressBar_NameUpdateDialogFrag);
        user = FirebaseAuth.getInstance().getCurrentUser();

        currentNameTxt.getEditText().setText(user.getDisplayName());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateName();
            }
        });

    }

    private void updateName(){
        String newName = newNameTxt.getEditText().getText().toString().toUpperCase();
        if (newName.length() > 0){
            progressBar.setVisibility(View.VISIBLE);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Name Succesfully updated", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            getDialog().dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Name can not be Change", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        }else{
            Toast.makeText(getContext(), "Please enter new name", Toast.LENGTH_SHORT).show();
        }


    }




}
