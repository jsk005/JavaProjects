package com.link2me.android.pdfviewer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.pdfviewer.databinding.ItemPdfBinding;
import com.link2me.android.pdfviewer.model.Pdf_Item;

import java.util.ArrayList;

public class BindPdfViewListAdapter extends RecyclerView.Adapter<BindPdfViewListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    private ArrayList<Pdf_Item> rvItemList;

    // 인터페이스 선언 -------------------------------------------------------------
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, Pdf_Item item, int position);
    }

    public void setOnItemSelectClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public BindPdfViewListAdapter(Context context, ArrayList<Pdf_Item> items){
        this.context = context;
        rvItemList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPdfBinding binding = ItemPdfBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(rvItemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return rvItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPdfBinding itemBinding;

        public ViewHolder(@NonNull ItemPdfBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bindItem(Pdf_Item item, int position){
            itemBinding.category.setText(item.getCategory());
            itemBinding.title.setText(item.getTitle());
            itemBinding.pdfurl.setText(item.getPdfurl());

            itemBinding.pdfLayout.setOnClickListener(view1 -> {
                if(mListener != null){
                    mListener.onItemClicked(view1,item,position);
                    // 클릭의 결과로 값을 전달
                    Log.d(TAG,"item clicked : "+position);
                }
            });
        }

    }
}
