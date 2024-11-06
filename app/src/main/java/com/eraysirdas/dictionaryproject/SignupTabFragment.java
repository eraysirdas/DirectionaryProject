package com.eraysirdas.dictionaryproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eraysirdas.dictionaryproject.databinding.FragmentSignupTabBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignupTabFragment extends Fragment {
    private FragmentSignupTabBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpOperation();
            }
        });

    }

    private void signUpOperation() {

        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        if(password.isEmpty() || email.isEmpty()){
            Toast.makeText(getActivity(), "Boş Alanları doldur.", Toast.LENGTH_LONG).show();
        }else{
            if(password.length()<6){
                Toast.makeText(getActivity(), "Şifre en az 6 haneli olmalı.", Toast.LENGTH_LONG).show();
            }else{

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(getActivity(),"Kayıt Başarılı",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Hata",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}