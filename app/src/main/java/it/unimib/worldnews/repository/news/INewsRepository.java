package it.unimib.worldnews.repository.news;

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
