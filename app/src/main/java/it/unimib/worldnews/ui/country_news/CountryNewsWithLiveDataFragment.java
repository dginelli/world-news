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
import androidx.lifecycle.MutableLiveData;
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

/**
 * It shows the news by country using the ViewModel and LiveData classes.
 */
public class CountryNewsWithLiveDataFragment extends Fragment {
    private static final String TAG = "CountryNewsLiveDataFrag";

    private NewsRecyclerViewAdapter mRecyclerViewNewsAdapter;
    private ProgressBar mProgressBar;

    private CountryNewsViewModel mCountryNewsViewModel;

    private List<News> mNewsList;

    private int totalItemCount; // Total number of news
    private int lastVisibleItem; // The position of the last visible news item
    private int visibleItemCount; // Number or total visible news items

    // Based on this value, the process of loading more news is anticipated or postponed
    // Look at the if condition at line 119 to see how it is used
    private final int threshold = 1;

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

        // Two different ways to create the CountryNewsViewModel object:
        // 1) Standard
        // 2) Custom that uses the class CountryNewsViewModelFactory
        mCountryNewsViewModel = new ViewModelProvider(requireActivity()).get(CountryNewsViewModel.class);
        /*mCountryNewsViewModel = new ViewModelProvider(requireActivity(), new CountryNewsViewModelFactory(
                requireActivity().getApplication(),
                mSharedPreferencesProvider.getCountry())).get(CountryNewsViewModel.class);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country_news_recyclerview, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        RecyclerView mRecyclerViewCountryNews = view.findViewById(R.id.recyclerview_country_news);
        mRecyclerViewCountryNews.setLayoutManager(layoutManager);

        mRecyclerViewNewsAdapter = new NewsRecyclerViewAdapter(mNewsList,
                news -> {
                    Log.d(TAG, "onItemClick: " + news);
                    CountryNewsWithLiveDataFragmentDirections.ActionCountryNewsToCountryNewsDetailFragment
                            action = CountryNewsWithLiveDataFragmentDirections.actionCountryNewsToCountryNewsDetailFragment(news);
                    Navigation.findNavController(view).navigate(action);
                }
        );

        mRecyclerViewCountryNews.setAdapter(mRecyclerViewNewsAdapter);

        mRecyclerViewCountryNews.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean isConnected = isConnected();

                if (isConnected && totalItemCount != mCountryNewsViewModel.getTotalResult()) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (totalItemCount == visibleItemCount || (
                            totalItemCount <= (lastVisibleItem + threshold) && dy > 0 && !mCountryNewsViewModel.isLoading()) &&
                            mCountryNewsViewModel.getNews().getValue() != null &&
                            mCountryNewsViewModel.getCurrentResults() != mCountryNewsViewModel.getNews().getValue().getTotalResults()
                    ) {
                        NewsResponse articleListResponse = new NewsResponse();

                        MutableLiveData<NewsResponse> newsListMutableLiveData = mCountryNewsViewModel.getNewsResponseLiveData();

                        if (newsListMutableLiveData.getValue() != null) {

                            mCountryNewsViewModel.setLoading(true);

                            List<News> currentArticleList = newsListMutableLiveData.getValue().getArticles();

                            // It adds a null element to enable the visualization of the loading item
                            // (it is managed by the class NewsRecyclerViewAdapter)
                            currentArticleList.add(null);
                            articleListResponse.setArticles(currentArticleList);
                            articleListResponse.setStatus(newsListMutableLiveData.getValue().getStatus());
                            articleListResponse.setTotalResults(newsListMutableLiveData.getValue().getTotalResults());
                            articleListResponse.setLoading(true);
                            newsListMutableLiveData.setValue(articleListResponse);

                            int page = mCountryNewsViewModel.getPage() + 1;
                            mCountryNewsViewModel.setPage(page);

                            mCountryNewsViewModel.getMoreNewsResource();
                        }
                    }
                }
            }
        });

        // The Observer associated with the LiveData to show the news
        final Observer<NewsResponse> observer = new Observer<NewsResponse>() {
            @Override
            public void onChanged(NewsResponse newsResponse) {
                if (newsResponse.isError()) {
                    updateUIForFailure(newsResponse.getStatus());
                }
                if (newsResponse.getArticles() != null && newsResponse.getTotalResults() != -1) {
                    mCountryNewsViewModel.setTotalResult(newsResponse.getTotalResults());
                        updateUIForSuccess(newsResponse.getArticles(), newsResponse.isLoading());
                } else {
                        updateUIForFailure(newsResponse.getStatus());
                }
            }
        };

        // It associates the LiveData object (obtained with the instruction mCountryNewsViewModel.getNews())
        // to the LiveData object.
        mCountryNewsViewModel.getNews().observe(getViewLifecycleOwner(), observer);

        return view;
    }

    /**
     * It shows the news retrieved by the Repository.
     * @param newsList the list of news to be shown.
     * @param isLoading true when the progress bar is visible, false otherwise.
     */
    private void updateUIForSuccess(List<News> newsList, boolean isLoading) {
        mNewsList.clear();
        mNewsList.addAll(newsList);

        if (!isLoading) {
            mCountryNewsViewModel.setLoading(false);
            mCountryNewsViewModel.setCurrentResults(newsList.size());
        }

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewNewsAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * It shows a warning message to the user with a Snackbar.
     * @param message The warning message to be shown in the Snackbar.
     */
    private void updateUIForFailure(String message) {
        Log.d(TAG, "Web Service call failed: " + message);
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.country_news_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (isConnected()) {
                totalItemCount = 0;
                mCountryNewsViewModel.refreshNews();
            } else {
                showError(getString(R.string.no_internet));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * It shows a SnackBar with an error message.
     * @param errorMessage The error message to be shown.
     */
    private void showError(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * It checks if the device is connected to Internet.
     * See: https://developer.android.com/training/monitoring-device-state/connectivity-status-type#DetermineConnection
     * @return true if the device is connected to Internet; false otherwise.
     */
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
