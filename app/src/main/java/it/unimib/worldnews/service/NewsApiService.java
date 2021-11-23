package it.unimib.worldnews.service;

import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.utils.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface NewsApiService {
    @GET(Constants.TOP_HEADLINES_ENDPOINT)
    Call<NewsResponse> getNews(
            @Query(Constants.TOP_HEADLINES_COUNTRY_PARAMETER) String country,
            @Header("Authorization") String apiKey);
}
