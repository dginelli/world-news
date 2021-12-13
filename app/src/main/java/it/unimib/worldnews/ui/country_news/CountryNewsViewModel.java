package it.unimib.worldnews.ui.country_news;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.repository.INewsRepositoryWithLiveData;
import it.unimib.worldnews.repository.NewsRepositoryWithLiveData;
import it.unimib.worldnews.utils.Constants;
import it.unimib.worldnews.utils.SharedPreferencesProvider;

/**
 * ViewModel to manage the list of news.
 */
public class CountryNewsViewModel extends AndroidViewModel {

    private static final String TAG = "CountryNewsViewModel";

    private final INewsRepositoryWithLiveData mINewsRepository;
    private MutableLiveData<NewsResponse> mNewsResponseLiveData;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private final String country;

    private int page = 1;
    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public MutableLiveData<NewsResponse> getNewsResponseLiveData() {
        return mNewsResponseLiveData;
    }

    /**
     * Available constructor since CountryNewsViewModel extends AndroidViewModel.
     * @param application The global application state.
     */
    public CountryNewsViewModel(Application application) {
        super(application);

        // You can choose which type of Repository to use
        mINewsRepository = new NewsRepositoryWithLiveData(application);
        /*mINewsRepository =
                new NewsMockRepositoryWithLiveData(application, INewsRepositoryWithLiveData.JsonParser.GSON);*/
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        this.country = mSharedPreferencesProvider.getCountry();
    }

    /**
     * Custom constructor that can be used thanks to
     * @param application The global application state.
     * @param country The country of interest.
     */
    public CountryNewsViewModel(Application application, String country) {
        super(application);

        // You can choose which type of Repository to use
        mINewsRepository = new NewsRepositoryWithLiveData(application);
        /*mINewsRepository =
                new NewsMockRepositoryWithLiveData(application, INewsRepositoryWithLiveData.JsonParser.GSON);*/
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        this.country = country;
    }

    /**
     * Method to pass the LiveData object associated with the
     * news list to the Fragment/Activity.
     * @return The LiveData object associated with the news list.
     */
    public MutableLiveData<NewsResponse> getNews() {

        if (mNewsResponseLiveData == null) {
            Log.d(TAG, "getNews: it's null");
            fetchNews();
        } else {
            mNewsResponseLiveData.getValue().setError(false);
            Log.d(TAG, "getNews: it's not null");
        }
        return mNewsResponseLiveData;
    }

    /**
     * It is used to download news every time
     * the user scrolls the list.
     */
    public void getMoreNewsResource() {
        mINewsRepository.fetchMoreNews(country, page);
    }

    /**
     * It downloads the latest news.
     */
    public void refreshNews() {
        setPage(1);
        setCurrentResults(0);
        setTotalResult(0);
        mINewsRepository.refreshNews(country, page);
    }

    /**
     * It uses the Repository to download the news list
     * and to associate it with the LiveData object.
     */
    private void fetchNews() {
        mNewsResponseLiveData = mINewsRepository.fetchNews(country,
                page, mSharedPreferencesProvider.getLastUpdate());
    }
}
