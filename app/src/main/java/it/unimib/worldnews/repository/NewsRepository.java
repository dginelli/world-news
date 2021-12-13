package it.unimib.worldnews.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.database.NewsDao;
import it.unimib.worldnews.database.NewsRoomDatabase;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.service.NewsApiService;
import it.unimib.worldnews.utils.Constants;
import it.unimib.worldnews.utils.ResponseCallback;
import it.unimib.worldnews.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository to get the news using the API
 * provided by NewsApi.org (https://newsapi.org).
 */
public class NewsRepository implements INewsRepository {

    private static final String TAG = "NewsRepository";

    private final Application mApplication;
    private final NewsApiService mNewsApiService;
    private final NewsDao mNewsDao;
    private final ResponseCallback mResponseCallback;

    public NewsRepository(Application application, ResponseCallback responseCallback) {
        this.mApplication = application;
        this.mNewsApiService = ServiceLocator.getInstance().getNewsApiService();
        NewsRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getNewsDao(application);
        this.mNewsDao = newsRoomDatabase.newsDao();
        this.mResponseCallback = responseCallback;
    }

    @Override
    public void fetchNews(String country, int page, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than one minute ago
        if (currentTime - lastUpdate > Constants.FRESH_TIMEOUT) {
            Call<NewsResponse> newsResponseCall = mNewsApiService.getNews(country, Constants.MAX_NEWS_RESULTS_PER_PAGE, page, Constants.NEWS_API_KEY);

            newsResponseCall.enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                    if (response.body() != null && response.isSuccessful() && !response.body().getStatus().equals("error")) {
                        List<News> newsList = response.body().getArticles();
                        saveDataInDatabase(newsList);
                        mResponseCallback.onResponse(newsList, System.currentTimeMillis());
                    } else {
                        mResponseCallback.onFailure(mApplication.getString(R.string.error_retrieving_news));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                    mResponseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            Log.d(TAG, "Data read from the local database");
            readDataFromDatabase(lastUpdate);
        }
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

    /**
     * It gets the news from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mResponseCallback.onResponse(mNewsDao.getAll(), lastUpdate);
            }
        };
        new Thread(runnable).start();
    }
}
