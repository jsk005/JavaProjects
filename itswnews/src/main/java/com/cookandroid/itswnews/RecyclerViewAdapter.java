package com.cookandroid.itswnews;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<NewsData> mDataset;

    public RecyclerViewAdapter(Context context, List<NewsData> myDataset) {
        mDataset = myDataset;
        Fresco.initialize(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_title;
        public TextView TextView_content;
        public SimpleDraweeView ImageView_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView_title = itemView.findViewById(R.id.TextView_title);
            TextView_content = itemView.findViewById(R.id.TextView_content);
            ImageView_title = itemView.findViewById(R.id.ImageView_title);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.e(TAG,"onBindViewHolder: called."); // 실제로 데이터를 표시하는 부분
        NewsData news = mDataset.get(position);

        holder.TextView_title.setText(news.getTitle());
        String content = news.getDescription();
        if(news.getDescription() != "null"){
            holder.TextView_content.setText(content);
        } else{
            holder.TextView_content.setText("ㅡ");
        }
        Uri uri = Uri.parse(news.getUrlToImage());

        holder.ImageView_title.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
