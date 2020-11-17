package com.example.chcook.KahHeng.EndUser.GUI.VideoAndRecipeManagement;


import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
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

public class UploadCookingStep extends Fragment {
    private View view;
    private ImageView imageView;
    private String imagekey;
    private String recipeKey="";
    private String imageURL;

    private EditText editTextStepDesc;
    private Button btnUploadStep;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CookingSteps cookingStep;
    private StorageReference imageRef;
    private FirebaseAuth firebaseAuth;

    public UploadCookingStep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_cooking_step, container, false);
        imageView = view.findViewById(R.id.imageViewStep);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            imagekey = bundle.getString("key");
            recipeKey=bundle.getString("Recipekey");
            Glide.with(getContext())
                    .asBitmap()
                    .load(imagekey)
                    .into(imageView);
            imageURL=imagekey;
        }
        editTextStepDesc = view.findViewById(R.id.editTxtStepDesc);
        btnUploadStep=view.findViewById(R.id.btnUploadStep);
        editTextStepDesc.setEnabled(true);

        btnUploadStep.setEnabled(true);
        btnUploadStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Please wait few second for uploading", Toast.LENGTH_SHORT).show();
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
        cookingStep=new CookingSteps();

        final String desc=editTextStepDesc.getText().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        String uid=firebaseAuth.getCurrentUser().getUid();


        if (imageURL != null && !desc.isEmpty()&&!recipeKey.isEmpty()) {
            cookingStep.setDescription(desc);
            cookingStep.setImageUrl(imageURL);

            cookingStep.getRecipes().setRecipeId(recipeKey);

            imageRef = storageReference.child("/Image/" + uid + "/" + System.currentTimeMillis() + "."+getFileExtension());
            UploadTask uploadTask = imageRef.putFile(Uri.parse(imageURL));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Network connection has problem", Toast.LENGTH_SHORT).show();
                    editTextStepDesc.setEnabled(true);
                    btnUploadStep.setEnabled(true);
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
                                     cookingStep.setImageUrl(uri.toString());
                                    CookingStepDA cookingStepDA=new CookingStepDA();
                                    cookingStepDA.createStep(cookingStep);

                                }

                            });

                        }

                    }
                    Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.myNavHostFragment, new RecipeManagementUI());
                    fragmentTransaction.addToBackStack(null).commit();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                    btnUploadStep.setEnabled(false);
                    editTextStepDesc.setEnabled(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please fill in the information", Toast.LENGTH_SHORT).show();
        }


    }


    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long fileSize = taskSnapshot.getTotalByteCount();
        long uploadbytes = taskSnapshot.getBytesTransferred();
        long progress = (100 * uploadbytes) / fileSize;
        ProgressBar progressBar = view.findViewById(R.id.progressBarUploadStep);
        progressBar.setProgress((int) progress);
    }


}