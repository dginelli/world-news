package it.unimib.worldnews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

/**
 * RecyclerView Adapter to show the news in a RecyclerView
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    // To detect a click on the RecyclerView items
    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    private final List<News> mNewsList;
    private final OnItemClickListener mOnItemClickListener;

    public NewsRecyclerViewAdapter(List<News> newsList, OnItemClickListener onItemClickListener) {
        this.mNewsList = newsList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.country_news_list_item, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mNewsList != null) {
            return mNewsList.size();
        }

        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNewsTitle;
        private final TextView textViewNewsSource;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewNewsTitle = itemView.findViewById(R.id.news_title);
            this.textViewNewsSource = itemView.findViewById(R.id.news_source);
        }

        public void bind(News news) {
            this.textViewNewsTitle.setText(news.getTitle());
            this.textViewNewsSource.setText(news.getNewsSource() != null ? news.getNewsSource().getName() : null);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(news);
                }
            });
        }
    }
}
