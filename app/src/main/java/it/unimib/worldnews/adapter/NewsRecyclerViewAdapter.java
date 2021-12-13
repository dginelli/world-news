package it.unimib.worldnews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.utils.DateTimeUtil;

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

        private final ImageView imageViewNews;
        private final TextView textViewNewsTitle;
        private final TextView textViewNewsDescription;
        private final TextView textViewNewsDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNews = itemView.findViewById(R.id.article_image);
            textViewNewsTitle = itemView.findViewById(R.id.news_title);
            textViewNewsDescription = itemView.findViewById(R.id.news_description);
            textViewNewsDate = itemView.findViewById(R.id.news_date);
        }

        public void bind(News news) {

            if (news != null) {

                String url = news.getUrlToImage();
                String newUrl = null;

                if (url != null) {
                    // This action is a possible alternative to manage HTTP addresses that don't work
                    // in the apps that target API level 28 or higher.
                    // If it doesn't work, the other option is this one:
                    // https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
                    newUrl = url.replace("http://", "https://").trim();

                    // Download the image associated with the article
                    Glide.with(itemView.getContext()).load(newUrl).
                            placeholder(R.drawable.ic_baseline_cloud_download_24).into(imageViewNews);
                }

                textViewNewsTitle.setText(news.getTitle());
                textViewNewsDescription.setText(news.getDescription());
                textViewNewsDate.setText(DateTimeUtil.getDate(news.getPublishedAt()));

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
