package it.unimib.worldnews.model;

import java.util.List;

/**
 * It represents the user preferences.
 */
public class UserPreference {
    private String favoriteCountry;
    private List<String> favoriteTopics;

    public UserPreference() {}

    public UserPreference(String favoriteCountry, List<String> favoriteTopics) {
        this.favoriteCountry = favoriteCountry;
        this.favoriteTopics = favoriteTopics;
    }

    public String getFavoriteCountry() {
        return favoriteCountry;
    }

    public void setFavoriteCountry(String favoriteCountry) {
        this.favoriteCountry = favoriteCountry;
    }

    public List<String> getFavoriteTopics() {
        return favoriteTopics;
    }

    public void setFavoriteTopics(List<String> favoriteTopics) {
        this.favoriteTopics = favoriteTopics;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "favoriteCountry='" + favoriteCountry + '\'' +
                ", favoriteTopics=" + favoriteTopics +
                '}';
    }
}
