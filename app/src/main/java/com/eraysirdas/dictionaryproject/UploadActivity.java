package com.eraysirdas.dictionaryproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eraysirdas.dictionaryproject.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {
    private ActivityUploadBinding binding;
    private FirebaseFirestore firestore;
    private String word,meaning,user,uid;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String info,inWord,inMeaningWord,inUser,dataId;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelime Paylaş");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Geri butonunu göster
        }

        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        inWord = intent.getStringExtra("inWord");
        inMeaningWord = intent.getStringExtra("inMeaningWord");
        inUser = intent.getStringExtra("inUser");
        dataId = intent.getStringExtra("dataId");


        if(info.equals("new")){
            binding.wordEt.setText("");
            binding.meaningEt.setText("");
            binding.userEt.setText("");
        }else{
            binding.wordEt.setText(inWord);
            binding.meaningEt.setText(inMeaningWord);
            binding.userEt.setText(inUser);
        }

        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString("username", "");
        boolean isChecked = sharedPreferences.getBoolean("isChecked", false);

        if (!savedUsername.isEmpty()) {
            binding.userEt.setText(savedUsername);
            binding.saveUsernameCheckBox.setChecked(isChecked);

            if (isChecked) {
                binding.userEt.setEnabled(false);
            } else {
                binding.userEt.setEnabled(true);
            }
        }

        binding.saveUsernameCheckBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            if (isChecked1) {
                binding.userEt.setEnabled(false);
            } else {
                binding.userEt.setEnabled(true);
            }
        });
    }

    public void uploadBtnClicked(View view){
        progressDialog.setMessage("Lütfen bekleyin...");
        progressDialog.show();
        word=binding.wordEt.getText().toString().trim();
        meaning=binding.meaningEt.getText().toString().trim();
        user=binding.userEt.getText().toString().trim();

        boolean isChecked = binding.saveUsernameCheckBox.isChecked();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isChecked) {
            editor.putString("username", user);
            editor.putBoolean("isChecked", true);
            binding.userEt.setEnabled(false);
        } else {
            editor.putBoolean("isChecked", false);
            binding.userEt.setEnabled(true);
        }

        editor.apply();

        String getUid = firebaseUser.getUid();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("word",word);
        hashMap.put("wordMeaning",meaning);
        hashMap.put("user",user);
        hashMap.put("date", FieldValue.serverTimestamp());
        hashMap.put("uid",getUid);

        if(info.equals("new")){

            firestore.collection("Data").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(UploadActivity.this, "Yüklendi.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(UploadActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Database Hatası.", Toast.LENGTH_LONG).show();
                }
            });

        }else {
            // Eğer info "edit" ise mevcut veriyi güncelle

            // Firestore'da ilgili dokümanı güncelleme
            firestore.collection("Data").document(dataId).update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UploadActivity.this, "Güncelleme Başarılı", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Hata! Veri güncellenemedi.", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Geri butona tıklanırsa
            onBackPressed(); // Önceki activity'ye geri dön
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}