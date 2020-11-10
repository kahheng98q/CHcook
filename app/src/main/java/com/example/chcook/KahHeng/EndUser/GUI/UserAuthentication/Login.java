package com.example.chcook.KahHeng.EndUser.GUI.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chcook.KahHeng.EndUser.GUI.MainPage;
import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.StaffLoginAndManagement.StaffLogin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    public static final int Google_Sign_In_Code = 10005;
    SignInButton signInButton;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton StaffLoginPage;


//    GoogleSignInAccount signInAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton=findViewById(R.id.sign_in_button);

        firebaseAuth=FirebaseAuth.getInstance();
        StaffLoginPage = findViewById(R.id.staffPageBtn);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1075307792110-f5fntm9j4e8rjqq79bl51t91ll6vacor.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient= GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount=GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null|| firebaseAuth.getCurrentUser()!=null){
            Toast.makeText(this,"User is logged in .", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainPage.class));
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                                if(this.signInAccount==null){

//                                                }
//                                                checkEmail();
                                                Intent sign= signInClient.getSignInIntent();
                                                startActivityForResult(sign,Google_Sign_In_Code);

    }
}

        );
        StaffLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Staff Login");
                builder.setMessage("Are you sure want to login as staff?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), StaffLogin.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Google_Sign_In_Code){
            final Task<GoogleSignInAccount> signInTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount signInAcc=signInTask.getResult(ApiException.class);
                AuthCredential authCredential= GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


//                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference ref = database.getReference("Users");

//                        DatabaseReference postsRef = ref.child(firebaseAuth.getCurrentUser().getUid());
//                        postsRef.setValue(new User(null,firebaseAuth.getCurrentUser().getEmail(),null,null,null));

                        //                       Log.d() ;
//                        Log.d("tag","onCreate "+ref.orderByChild(firebaseAuth.getCurrentUser().getEmail()));
//                        ref.push().setValue(new User(null, signInAcc.getEmail(), null, null, null));
//                        Toast.makeText(getApplicationContext(),"User is logged in has .", Toast.LENGTH_SHORT).show();
//                        FirebaseUser user=firebaseAuth.getCurrentUser();
//                        updateUI(user);
                        startActivity(new Intent(getApplicationContext(),MainPage.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                Toast.makeText(this,"Connected.", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    public  void updateUI(FirebaseUser user){
        GoogleSignInAccount acct=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(acct!=null){
            Toast.makeText(this,"SSSSSSSSSSSs.", Toast.LENGTH_SHORT).show();
        }
    }
//    public void checkEmail(){
////        firebaseAuth.fetchSignInMethodsForEmail(firebaseAuth.getCurrentUser().getEmail())
////        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
////
////            @Override
////            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
////                boolean check =!task.getResult().getSignInMethods().isEmpty();
////                if (!check){
////                    Toast.makeText(getApplicationContext(),"Empty.", Toast.LENGTH_SHORT).show();
////                }
////
////            }
////        });
//    }
}
