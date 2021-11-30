package it.unimib.worldnews.ui.country_news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.adapter.NewsRecyclerViewAdapter;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.utils.SharedPreferencesProvider;

public class CountryNewsWithLiveDataFragment extends Fragment {
    private static final String TAG = "CountryNewsLiveDataFrag";

    private NewsRecyclerViewAdapter mRecyclerViewNewsAdapter;
    private ProgressBar mProgressBar;

    private CountryNewsViewModel mCountryNewsViewModel;

    private List<News> mNewsList;

    public CountryNewsWithLiveDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (mNewsList == null) {
            mNewsList = new ArrayList<>();
        }

        SharedPreferencesProvider mSharedPreferencesProvider = new SharedPreferencesProvider(requireActivity().getApplication());

        mCountryNewsViewModel = new ViewModelProvider(requireActivity()).get(CountryNewsViewModel.class);
        /*mCountryNewsViewModel = new ViewModelProvider(requireActivity(), new CountryNewsViewModelFactory(
                requireActivity().getApplication(),
                mSharedPreferencesProvider.getCountry())).get(CountryNewsViewModel.class);*/

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
                    CountryNewsWithLiveDataFragmentDirections.ActionCountryNewsToCountryNewsDetailFragment
                            action = CountryNewsWithLiveDataFragmentDirections.actionCountryNewsToCountryNewsDetailFragment(news);
                    Navigation.findNavController(view).navigate(action);
                }
        );

        mRecyclerViewCountryNews.setAdapter(mRecyclerViewNewsAdapter);

        final Observer<NewsResponse> observer = new Observer<NewsResponse>() {
            @Override
            public void onChanged(NewsResponse newsResponse) {
                if (newsResponse.isError()) {
                    showError(newsResponse.getStatus());
                }
                mNewsList.clear();
                if (newsResponse.getArticles() != null) {
                    mNewsList.addAll(newsResponse.getArticles());
                    mRecyclerViewNewsAdapter.notifyDataSetChanged();
                }

                mProgressBar.setVisibility(View.GONE);
            }
        };

        mProgressBar.setVisibility(View.VISIBLE);
        mCountryNewsViewModel.getNews().observe(getViewLifecycleOwner(), observer);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.country_news_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (isConnected()) {
                mProgressBar.setVisibility(View.VISIBLE);
                mCountryNewsViewModel.refreshNews();
            } else {
                showError(getString(R.string.no_internet));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showError(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
