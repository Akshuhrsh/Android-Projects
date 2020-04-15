package com.example.inclass12;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpScreen extends Fragment {

    EditText firstName,lastName,email,pwd,pwdConfirm;
    Button signUpButton,cancelButton;
    ProgressBar progressBar;

    FromSignUpToLogin fromSignUpToLogin;

    SaveUserRecordInFirebase saveUserRecordInFirebase;
    private FirebaseAuth mAuth;


    public SignUpScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromSignUpToLogin=(FromSignUpToLogin) getActivity();
        saveUserRecordInFirebase=(SaveUserRecordInFirebase) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_screen, container, false);
        firstName=view.findViewById(R.id.fname_Et);
        lastName=view.findViewById(R.id.lname_Et);
        email=view.findViewById(R.id.email_Et);
        pwd=view.findViewById(R.id.pwd_Et);
        pwdConfirm=view.findViewById(R.id.confPwd_Et);
        signUpButton=view.findViewById(R.id.signupButton);
        cancelButton=view.findViewById(R.id.cancelButton);
        progressBar=view.findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                && !pwd.getText().toString().isEmpty() && !pwdConfirm.getText().toString().isEmpty()){
                    if(pwd.getText().toString().equals(pwdConfirm.getText().toString())){
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            User userRecord=new User();
                                            userRecord.setuID(user.getUid());
                                            saveUserRecordInFirebase.saveUserRecord(userRecord);
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "User creation failure"+task.getException(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                    else{
                        Toast.makeText(getActivity(), "(The given password and the repeated password must match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Enter values for all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromSignUpToLogin.gotoLogin();
            }
        });

        return view;
    }

    public interface FromSignUpToLogin{
        void gotoLogin();
    }

    public interface SaveUserRecordInFirebase{
        void saveUserRecord(User user);
    }

}
