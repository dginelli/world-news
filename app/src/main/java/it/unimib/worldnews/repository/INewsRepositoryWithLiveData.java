package it.unimib.worldnews.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.NewsResponse;

/**
 * Interface for Repositories that manage News objects with LiveData.
 */
public interface INewsRepositoryWithLiveData {

    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    MutableLiveData<NewsResponse> fetchNews(String country, int page, long lastUpdate);
    void fetchMoreNews(String country, int page);
    void refreshNews(String country, int page);
}
