package it.unimib.worldnews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

/**
 * RecyclerView Adapter to show the news in a RecyclerView.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NEWS_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

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

    @Override
    public int getItemViewType(int position) {
        if (mNewsList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return NEWS_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = null;

        if (viewType == NEWS_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.country_news_list_item, parent, false);
            return new NewsViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.news_loading_item, parent, false);
            return new LoadingNewsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).bind(mNewsList.get(position));
        } else if (holder instanceof LoadingNewsViewHolder) {
            ((LoadingNewsViewHolder) holder).activate();
        }
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

            if (news != null) {
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

    public static class LoadingNewsViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingNewsViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressbar_loading_news);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }
}
