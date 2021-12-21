package it.unimib.worldnews.model;

/**
 * It represents a User saved into Firebase Realtime Database.
 */
public class User {
    private String uId;
    private String email;
    private UserPreference userPreference;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uId, String email) {
        this.email = email;
    }

    public User(String uId, String email, UserPreference userPreference) {
        this(uId, email);
        this.userPreference = userPreference;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserPreference getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(UserPreference userPreference) {
        this.userPreference = userPreference;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId='" + uId + '\'' +
                ", email='" + email + '\'' +
                ", userPreference=" + userPreference +
                '}';
    }
}
