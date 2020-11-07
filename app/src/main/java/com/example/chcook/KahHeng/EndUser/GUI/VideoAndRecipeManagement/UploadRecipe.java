package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.Recipes;
import com.example.chcook.KahHeng.EndUser.DA.RecipeDA;
import com.example.chcook.KahHeng.EndUser.GUI.Home;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadRecipe extends Fragment {
    private View view;
    private ImageView imageView;
    private String imagekey;
    private String imageURL;
    private EditText editTextRecipeName;
    private EditText editTextRecipeDesc;
    private Button btnUploadRecipe;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Recipes recipe;
    private StorageReference imageRef;
    private FirebaseAuth firebaseAuth;

    public UploadRecipe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_recipe, container, false);
        imageView = view.findViewById(R.id.imageViewRecipe);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            imagekey = bundle.getString("key");
            Glide.with(getContext())
                    .asBitmap()
                    .load(imagekey)
                    .into(imageView);
            imageURL=imagekey;
        }
        editTextRecipeDesc = view.findViewById(R.id.editTxtRecipeDesc);
        editTextRecipeName = view.findViewById(R.id.editTxtRecipeName);
        btnUploadRecipe=view.findViewById(R.id.btnUploadRecipe);

        btnUploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return view;
    }

    private String getFileExtension(){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(imageURL)));
    }

    private void upload() {
        recipe=new Recipes();

        final String name=editTextRecipeName.getText().toString();
        final String desc=editTextRecipeDesc.getText().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        String uid=firebaseAuth.getCurrentUser().getUid();

        recipe.setTitle(name);
        recipe.setDescription(desc);
        recipe.setImageUrl(imageURL);

        if (imageURL != null && !desc.isEmpty() && !name.isEmpty()) {
            imageRef = storageReference.child("/Image/" + uid + "/" + System.currentTimeMillis() + "."+getFileExtension());
            UploadTask uploadTask = imageRef.putFile(Uri.parse(imageURL));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    recipe.setImageUrl(uri.toString());
                                    RecipeDA recipeDA=new RecipeDA();
                                    recipeDA.createRecipe(recipe);
                                }

                            });

                        }

                    }
                    Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    fragmentManager =getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new Home());
                    fragmentTransaction.addToBackStack(null).commit();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
//                    desctxt.setEnabled(false);
//                    nametxt.setEnabled(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Video and fill in the information", Toast.LENGTH_SHORT).show();
        }


    }


    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long fileSize = taskSnapshot.getTotalByteCount();
        long uploadbytes = taskSnapshot.getBytesTransferred();
        long progress = (100 * uploadbytes) / fileSize;
        ProgressBar progressBar = view.findViewById(R.id.progressBarUploadRecipe);
        progressBar.setProgress((int) progress);
    }

}
