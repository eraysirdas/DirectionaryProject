package com.eraysirdas.dictionaryproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eraysirdas.dictionaryproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseFirestore firestore;
    ArrayList<Data> dataArrayList;
    AdapterRecycler adapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dataArrayList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        getData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycler=new AdapterRecycler(dataArrayList);
        binding.recyclerView.setAdapter(adapterRecycler);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sözlük");
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {

        firestore.collection("Data")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dataArrayList.clear();  // Listeyi temizle
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                        String documentId = documentSnapshot.getId();

                        Map<String, Object> map = documentSnapshot.getData();
                        String word = (String) map.get("word");
                        String wordMeaning = (String) map.get("wordMeaning");
                        String user = (String) map.get("user");
                        Timestamp date = (Timestamp) map.get("date");

                        Data data = new Data(documentId, word, wordMeaning, user, date);
                        dataArrayList.add(data);
                    }
                    adapterRecycler.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Veriler yüklenemedi.", Toast.LENGTH_SHORT).show();
                });
    }

    public void fabBtnClicked(View view){
        Intent intent = new Intent(MainActivity.this,UploadActivity.class);
        intent.putExtra("info","new");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // SearchView'i yapılandırma
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Arama Yap");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return true;
    }

    private void filter(String text) {
        ArrayList<Data> filteredList = new ArrayList<>();

        for (Data item : dataArrayList) {
            if (item.getWord().toLowerCase().contains(text.toLowerCase()) || item.getWordMeaning().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyTextView.setVisibility(View.VISIBLE);
        } else {
            adapterRecycler.filterList(filteredList);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyTextView.setVisibility(View.GONE);
        }
    }

}