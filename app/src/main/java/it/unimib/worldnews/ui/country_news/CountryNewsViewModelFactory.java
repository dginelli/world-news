package it.unimib.worldnews.ui.country_news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CountryNewsViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private String country;

    public CountryNewsViewModelFactory(Application application, String country) {
        this.mApplication = application;
        this.country = country;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CountryNewsViewModel(mApplication, country);
    }
}
