package com.eraysirdas.dictionaryproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eraysirdas.dictionaryproject.databinding.RowRecyclerviewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.AdapterHolder> {

    ArrayList<Data> dataArrayList;
    private FirebaseFirestore firestore;


    public AdapterRecycler(ArrayList<Data> dataArrayList) {
        this.dataArrayList = dataArrayList;
        firestore=FirebaseFirestore.getInstance();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Data> filterList) {
        // below line is to add our filtered
        // list in our course array list.
        dataArrayList = filterList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRecyclerviewBinding rowRecyclerviewBinding = RowRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AdapterHolder(rowRecyclerviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.firstTv.setText(dataArrayList.get(position).getWord());
        holder.binding.secondTv.setText(dataArrayList.get(position).getWordMeaning());
        holder.binding.userTv.setText(dataArrayList.get(position).getUser());

        Timestamp postTimestamp = dataArrayList.get(position).getDate();
        String timeAgo = Utils.getTimeAgo(postTimestamp);
        holder.binding.dateTv.setText(timeAgo);


        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(holder,position);
            }
        });

        holder.binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),UploadActivity.class);
                intent.putExtra("info","update");
                intent.putExtra("inWord",dataArrayList.get(position).getWord());
                intent.putExtra("inMeaningWord",dataArrayList.get(position).getWordMeaning());
                intent.putExtra("inUser",dataArrayList.get(position).getUser());
                intent.putExtra("dataId",dataArrayList.get(position).getUid());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    private void deleteData(AdapterHolder holder, int pos) {
        firestore.collection("Data").document(dataArrayList.get(pos).getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Silme işlemi başarılı
                        Toast.makeText(holder.itemView.getContext(), "Veri Silindi", Toast.LENGTH_SHORT).show();
                        // RecyclerView'dan veriyi sil
                        dataArrayList.remove(dataArrayList.get(pos));  // Veriyi listeden çıkar
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Silme işlemi başarısız
                        Toast.makeText(holder.itemView.getContext(), "Silme işlemi başarısız", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }



    public static class AdapterHolder extends RecyclerView.ViewHolder{
        private  RowRecyclerviewBinding binding;
        public AdapterHolder(RowRecyclerviewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }


}
