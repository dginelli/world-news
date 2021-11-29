package it.unimib.worldnews.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

/**
 * It shows the news detail.
 */
public class CountryNewsDetailFragment extends Fragment {

    private static final String TAG = "CountryNewsDetailFrag";

    public CountryNewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        News news = CountryNewsDetailFragmentArgs.fromBundle(getArguments()).getNews();
        Log.d(TAG, news.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_news_detail, container, false);
    }
}
