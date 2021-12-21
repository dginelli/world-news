package it.unimib.worldnews.repository.news;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.database.NewsDao;
import it.unimib.worldnews.database.NewsRoomDatabase;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.repository.news.INewsRepositoryWithLiveData;
import it.unimib.worldnews.service.NewsApiService;
import it.unimib.worldnews.utils.Constants;
import it.unimib.worldnews.utils.ServiceLocator;
import it.unimib.worldnews.utils.SharedPreferencesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepositoryWithLiveData implements INewsRepositoryWithLiveData {

    private static final String TAG = "NewsRepWithLiveData";

    private final Application mApplication;
    private final MutableLiveData<NewsResponse> mNewsResponseLiveData;
    private final NewsApiService mNewsApiService;
    private final NewsDao mNewsDao;
    private final SharedPreferencesProvider mSharedPreferencesProvider;

    public NewsRepositoryWithLiveData(Application application) {
        this.mApplication = application;
        this.mNewsApiService = ServiceLocator.getInstance().getNewsApiService();
        this.mNewsResponseLiveData = new MutableLiveData<>();
        NewsRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getNewsDao(application);
        this.mNewsDao = newsRoomDatabase.newsDao();
        mSharedPreferencesProvider = new SharedPreferencesProvider(mApplication);
    }

    @Override
    public MutableLiveData<NewsResponse> fetchNews(String country, int page, long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than one minute ago
        if (currentTime - lastUpdate > Constants.FRESH_TIMEOUT) {
            getNews(country, page);
        } else {
            Log.d(TAG, "Data read from the local database");
            readDataFromDatabase(lastUpdate, null);
        }

        return mNewsResponseLiveData;
    }

    @Override
    public void fetchMoreNews(String country, int page) {
        Call<NewsResponse> call = mNewsApiService.getNews(country, Constants.MAX_NEWS_RESULTS_PER_PAGE, page, Constants.NEWS_API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.body() != null && response.isSuccessful() && !response.body().getStatus().equals("error")) {

                    List<News> newsList = response.body().getArticles();
                    addNews(newsList);

                    List<News> currentNewsList = mNewsResponseLiveData.getValue().getArticles();

                    // It removes the null element added to show the loading item in the RecyclerView
                    currentNewsList.remove(currentNewsList.size() - 1);
                    currentNewsList.addAll(newsList);

                    for (News news : newsList) {
                        Log.d(TAG, news.getTitle());
                    }

                    NewsResponse newResponse = new NewsResponse();
                    newResponse.setStatus(response.body().getStatus());
                    newResponse.setArticles(currentNewsList);
                    newResponse.setLoading(false);
                    newResponse.setTotalResults(response.body().getTotalResults());

                    mNewsResponseLiveData.postValue(newResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                mNewsResponseLiveData.postValue(new NewsResponse(t.getMessage(), -1, null, false));
            }
        });
    }

    @Override
    public void refreshNews(String country, int page) {
        getNews(country, page);
    }

    private void getNews(String country, int page) {

        Call<NewsResponse> newsResponseCall = mNewsApiService.getNews(country, Constants.MAX_NEWS_RESULTS_PER_PAGE, page, Constants.NEWS_API_KEY);

        newsResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.body() != null && response.isSuccessful() && !response.body().getStatus().equals("error")) {
                    saveDataInDatabase(response.body().getArticles());
                    // It changes the NewsResponse object associated with the LiveData
                    // in CountryNewsWithLiveDataFragment
                    mNewsResponseLiveData.postValue(response.body());
                    mSharedPreferencesProvider.setLastUpdate(System.currentTimeMillis());
                } else {
                    readDataFromDatabase(mSharedPreferencesProvider.getLastUpdate(),
                            mApplication.getString(R.string.error_retrieving_news));
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                readDataFromDatabase(mSharedPreferencesProvider.getLastUpdate(), t.getMessage());
            }
        });
    }

    /**
     * It saves the news in the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     * @param newsList the list of news to be written in the local database.
     */
    private void saveDataInDatabase(List<News> newsList) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNewsDao.deleteAll();
                mNewsDao.insertNewsList(newsList);
            }
        };
        new Thread(runnable).start();
    }

    private void addNews(List<News> articleList) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNewsDao.insertNewsList(articleList);
            }
        };
        new Thread(runnable).start();
    }

    /**
     * It gets the news from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate, String errorMessage) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                NewsResponse newsResponse = mNewsResponseLiveData.getValue();

                if (newsResponse == null) {
                    newsResponse = new NewsResponse();
                }
                newsResponse.setArticles(mNewsDao.getAll());

                if (errorMessage != null) {
                    newsResponse.setStatus(errorMessage);
                    newsResponse.setError(true);
                }

                // It changes the NewsResponse object associated to the LiveData
                // in CountryNewsWithLiveDataFragment
                mNewsResponseLiveData.postValue(newsResponse);

                //mResponseCallback.onResponse(mNewsDao.getAll(), lastUpdate);
            }
        };
        new Thread(runnable).start();
    }
}
