package com.eraysirdas.dictionaryproject;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eraysirdas.dictionaryproject.databinding.RowRecyclerviewBinding;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.AdapterHolder> {

    ArrayList<Data> dataArrayList;

    public AdapterRecycler(ArrayList<Data> dataArrayList) {
        this.dataArrayList = dataArrayList;
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
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        holder.binding.firstTv.setText(dataArrayList.get(position).word);
        holder.binding.secondTv.setText(dataArrayList.get(position).wordMeaning);
        holder.binding.userTv.setText(dataArrayList.get(position).user);

        Timestamp postTimestamp = dataArrayList.get(position).date;
        String timeAgo = Utils.getTimeAgo(postTimestamp);
        holder.binding.dateTv.setText(timeAgo);
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
