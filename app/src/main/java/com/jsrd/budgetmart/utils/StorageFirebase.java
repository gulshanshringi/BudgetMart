package com.jsrd.budgetmart.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jsrd.budgetmart.R;

public class StorageFirebase {
    private FirebaseStorage storage;
    private Context mContext;

    public StorageFirebase(Context context) {
        mContext = context;
        storage = FirebaseStorage.getInstance();
    }

    public void setImageToImageView(String imageLink, final ImageView imageView) {
        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference =
                storage.getReferenceFromUrl(imageLink);
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(mContext).
                        load(imageURL).
                        fitCenter().
                        into(imageView);
            }
        });
    }

}
