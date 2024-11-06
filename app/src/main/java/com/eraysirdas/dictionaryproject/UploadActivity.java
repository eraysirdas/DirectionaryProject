package com.eraysirdas.dictionaryproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eraysirdas.dictionaryproject.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private ActivityUploadBinding binding;
    private FirebaseFirestore firestore;
    private String word,meaning,user,uid;
    ProgressDialog progressDialog;
    String info,inWord,inMeaningWord,inUser,dataId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
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
        dataId = intent.getStringExtra("dataId"); // Güncellenecek dokümanın ID'sini alın




        if(info.equals("new")){
            binding.wordEt.setText("");
            binding.meaningEt.setText("");
            binding.userEt.setText("");
        }else{
            binding.wordEt.setText(inWord);
            binding.meaningEt.setText(inMeaningWord);
            binding.userEt.setText(inUser);
        }
    }

    public void uploadBtnClicked(View view){
        progressDialog.setMessage("Azıcık Bekle...");
        progressDialog.show();
        word=binding.wordEt.getText().toString().trim();
        meaning=binding.meaningEt.getText().toString().trim();
        user=binding.userEt.getText().toString().trim();


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("word",word);
        hashMap.put("wordMeaning",meaning);
        hashMap.put("user",user);
        hashMap.put("date", FieldValue.serverTimestamp());

        if(info.equals("new")){

            firestore.collection("Data").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(UploadActivity.this, "Tebrikler uaq aslanı veya kaplanı", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(UploadActivity.this, "Bozdun! Şaka bu sefer sorun sende değil bende :/", Toast.LENGTH_LONG).show();
                }
            });

        }else {
            // Eğer info "edit" ise mevcut veriyi güncelle

            // Firestore'da ilgili dokümanı güncelleme
            firestore.collection("Data").document(dataId).update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UploadActivity.this, "Veri başarıyla güncellendi!", Toast.LENGTH_LONG).show();
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