package it.unimib.worldnews.model;

import java.util.List;

/**
 * It is the response associated with the JSON received
 * by the following endpoint:
 * https://newsapi.org/docs/endpoints/top-headlines
 */
public class NewsResponse {
    private String status;
    private int totalResults;
    private List<News> articles;
    private boolean isError;

    public NewsResponse(String status, int totalResults, List<News> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public NewsResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
