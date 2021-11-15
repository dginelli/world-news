package it.unimib.worldnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

public class NewsListViewArrayAdapter extends ArrayAdapter<News> {

    private News[] mArrayNews;
    private int mLayout;

    public NewsListViewArrayAdapter(@NonNull Context context, int resource, @NonNull News[] objects) {
        super(context, resource, objects);
        this.mArrayNews = objects;
        this.mLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        TextView textViewNewsTitle = convertView.findViewById(R.id.news_title);
        TextView textViewSourceTitle = convertView.findViewById(R.id.news_source);

        textViewNewsTitle.setText(mArrayNews[position].getTitle());
        textViewSourceTitle.setText(mArrayNews[position].getNewsSource().getName());

        return convertView;
    }
}
