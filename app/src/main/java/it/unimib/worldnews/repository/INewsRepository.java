package it.unimib.worldnews.repository;

/**
 * Interface for Repositories that manage News objects.
 */
public interface INewsRepository {

    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    void fetchNews(String country, int page, long lastUpdate);
}
