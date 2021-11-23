package it.unimib.worldnews.utils;

import java.util.List;

import it.unimib.worldnews.model.News;

/**
 * Interface to send data from Repositories to Activity/Fragment.
 */
public interface ResponseCallback {
    void onResponse(List<News> newsList, long lastUpdate);
    void onFailure(String errorMessage);
}
