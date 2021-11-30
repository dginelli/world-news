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

public class CountryNewsViewModel extends AndroidViewModel {

    private static final String TAG = "CountryNewsViewModel";

    private final INewsRepositoryWithLiveData mINewsRepository;
    private MutableLiveData<NewsResponse> mNewsResponseLiveData;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private String country;

    public CountryNewsViewModel(Application application) {
        super(application);
        //mINewsRepository = new NewsRepositoryWithLiveData(application);
        mINewsRepository =
                new NewsMockRepositoryWithLiveData(application, INewsRepositoryWithLiveData.JsonParser.GSON);
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        this.country = mSharedPreferencesProvider.getCountry();
    }

    public CountryNewsViewModel(Application application, String country) {
        super(application);
        //mINewsRepository = new NewsRepositoryWithLiveData(application);
        mINewsRepository =
                new NewsMockRepositoryWithLiveData(application, INewsRepositoryWithLiveData.JsonParser.GSON);
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        this.country = country;
    }

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

    public void refreshNews() {
        mINewsRepository.refreshNews(country);
    }

    private void fetchNews() {
        mNewsResponseLiveData = mINewsRepository.fetchNews(country,
                Constants.MAX_RESULTS_PER_PAGE, mSharedPreferencesProvider.getLastUpdate());
    }
}
