package it.unimib.worldnews.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.NewsResponse;

public interface INewsRepositoryWithLiveData {

    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    MutableLiveData<NewsResponse> fetchNews(String country, int page, long lastUpdate);
    void refreshNews(String country);
}
