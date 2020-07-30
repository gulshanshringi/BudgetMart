package com.jsrd.budgetmart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.MainActivity;
import com.jsrd.budgetmart.activity.PhoneAuthActivity;

public class AccountFragment extends Fragment {

    private FirebaseUser user;
    private TextView logoutBtn;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).activityMainToolbar.setTitle("Account");

        logoutBtn = getView().findViewById(R.id.logoutBtn);
        setUpUserDeatils();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent PhoneAuthIntent = new Intent(getContext(), PhoneAuthActivity.class);
        startActivity(PhoneAuthIntent);
        getActivity().finish();
    }

    private void setUpUserDeatils() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView nameIcon, name, number;
        nameIcon = getView().findViewById(R.id.nameIcon);
        //nameIcon.setText(String.valueOf(user.getDisplayName().charAt(0)).toUpperCase());
        name = getView().findViewById(R.id.userName);
        number = getView().findViewById(R.id.userNumber);
        //name.setText(user.getDisplayName());
        number.setText(user.getPhoneNumber());
    }

    public void openUpdateEmailIdDialog() {
        EmailUpdateDialogFrag emailUpdateDialogFrag = new EmailUpdateDialogFrag();
        emailUpdateDialogFrag.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        emailUpdateDialogFrag.show(getFragmentManager(), "EmailUpdateDialogFrag");
    }

    public void openUpdateNameDialog() {
        NameUpdateDialogFrag nameUpdateDialogFrag = new NameUpdateDialogFrag();
        nameUpdateDialogFrag.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        nameUpdateDialogFrag.show(getFragmentManager(), "NameUpdateDialogFrag");

    }

    public void openUpdatePasswordDialog() {
        PasswordUpdateDialogFrag passwordUpdateDialogFrag = new PasswordUpdateDialogFrag();
        passwordUpdateDialogFrag.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        passwordUpdateDialogFrag.show(getFragmentManager(), "PasswordUpdateDialogFrag");

    }


}
