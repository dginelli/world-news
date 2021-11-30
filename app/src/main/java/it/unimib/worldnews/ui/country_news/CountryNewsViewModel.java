package it.unimib.worldnews.ui.country_news;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.repository.INewsRepositoryWithLiveData;
import it.unimib.worldnews.repository.NewsMockRepositoryWithLiveData;
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
    private String country;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
     * It downloads the latest news.
     */
    public void refreshNews() {
        mINewsRepository.refreshNews(country);
    }

    /**
     * It uses the Repository to download the news list
     * and to associate it with the LiveData object.
     */
    private void fetchNews() {
        mNewsResponseLiveData = mINewsRepository.fetchNews(country,
                Constants.MAX_RESULTS_PER_PAGE, mSharedPreferencesProvider.getLastUpdate());
    }
}
