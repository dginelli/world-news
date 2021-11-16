package it.unimib.worldnews.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

/**
 * Custom Adapter that extends BaseAdapter to show the news in a ListView.
 */
public class NewsListViewBaseAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<News> mListNews;

    public NewsListViewBaseAdapter(Activity mActivity, List<News> mListNews) {
        this.mActivity = mActivity;
        this.mListNews = mListNews;
    }

    @Override
    public int getCount() {
        if (mListNews != null) {
            return mListNews.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mListNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.country_news_list_item, parent, false);
        }

        TextView textViewNewsTitle = convertView.findViewById(R.id.news_title);
        TextView textViewSourceTitle = convertView.findViewById(R.id.news_source);

        textViewNewsTitle.setText(mListNews.get(position).getTitle());
        textViewSourceTitle.setText(mListNews.get(position).getNewsSource().getName());

        return convertView;
    }
}
