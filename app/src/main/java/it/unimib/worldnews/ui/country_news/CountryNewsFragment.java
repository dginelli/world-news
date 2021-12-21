package it.unimib.worldnews.ui.country_news;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.adapter.NewsListViewBaseAdapter;
import it.unimib.worldnews.adapter.NewsRecyclerViewAdapter;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsSource;
import it.unimib.worldnews.repository.news.INewsRepository;
import it.unimib.worldnews.utils.ResponseCallback;
import it.unimib.worldnews.utils.SharedPreferencesProvider;

/**
 * It shows the news by country.
 */
public class CountryNewsFragment extends Fragment implements ResponseCallback {

    private static final String TAG = "CountryNewsFragment";

    private News[] mNewsArray;
    private List<News> mNewsList;
    private NewsRecyclerViewAdapter mRecyclerViewNewsAdapter;
    private INewsRepository mINewsRepository;
    private NewsListViewBaseAdapter mNewsListViewBaseAdapter;
    private ProgressBar mProgressBar;
    private SharedPreferencesProvider mSharedPreferencesProvider;

    public CountryNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsArray = new News[20];
        mNewsList = new ArrayList<>();

        for (int i = 0; i < mNewsArray.length; i++) {
            mNewsArray[i] = new News(new NewsSource("Source"), null,
                    "Title " + i, null, null, null, null, null);
        }

        mSharedPreferencesProvider = new SharedPreferencesProvider(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country_news_recyclerview, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar);

        RecyclerView mRecyclerViewCountryNews = view.findViewById(R.id.recyclerview_country_news);
        mRecyclerViewCountryNews.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerViewNewsAdapter = new NewsRecyclerViewAdapter(mNewsList,
                news -> {
                    Log.d(TAG, "onItemClick: " + news);
                }
        );

        mRecyclerViewCountryNews.setAdapter(mRecyclerViewNewsAdapter);

        /* ###### Examples with ListView and different types of Adapters ###### */

        // Inflate the layout for this fragment
        /*View view = inflater.inflate(R.layout.fragment_country_news_listview, container, false);
        ListView mListViewCountryNews = view.findViewById(R.id.listview_country_news);*/

        // ArrayAdapter

        /*ArrayAdapter<News> mRecyclerViewNewsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mNewsArray);

        mListViewCountryNews.setAdapter(mRecyclerViewNewsAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        // Custom Adapter that extends ArrayAdapter

        /*NewsListViewArrayAdapter newsNewsListViewArrayAdapter =
                new NewsListViewArrayAdapter(requireContext(), R.layout.country_news_list_item, mNewsArray);

        mListViewCountryNews.setAdapter(newsNewsListViewArrayAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        // Custom Adapter that extends Base

        /*mNewsListViewBaseAdapter = new NewsListViewBaseAdapter(mNewsList);
        mListViewCountryNews.setAdapter(mNewsListViewBaseAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + mNewsList.get(position));
            }
        });*/

        mProgressBar.setVisibility(View.VISIBLE);
        mINewsRepository.fetchNews(mSharedPreferencesProvider.getCountry(), 0, mSharedPreferencesProvider.getLastUpdate());

        return view;
    }

    @Override
    public void onResponse(List<News> newsList, long lastUpdate) {

        if (newsList != null) {
            mNewsList.addAll(newsList);
            mSharedPreferencesProvider.setLastUpdate(lastUpdate);
        }

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewNewsAdapter.notifyDataSetChanged();
                //mNewsListViewBaseAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(requireActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
