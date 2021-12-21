package it.unimib.worldnews.ui.country_news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import it.unimib.worldnews.R;
import it.unimib.worldnews.databinding.FragmentCountryNewsDetailBinding;
import it.unimib.worldnews.model.News;

/**
 * It shows the news detail.
 */
public class CountryNewsDetailFragment extends Fragment {

    private static final String TAG = "CountryNewsDetailFrag";

    private FragmentCountryNewsDetailBinding mBinding;

    public CountryNewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        News news = CountryNewsDetailFragmentArgs.fromBundle(getArguments()).getNews();
        Log.d(TAG, news.toString());

        // Example of use of View Binding
        // https://developer.android.com/topic/libraries/view-binding
        mBinding = FragmentCountryNewsDetailBinding.inflate(getLayoutInflater(), container, false);

        View view = mBinding.getRoot();

        Glide.with(mBinding.imageviewNews.getContext()).load(news.getUrlToImage()).
                placeholder(R.drawable.ic_baseline_cloud_download_24).into(mBinding.imageviewNews);

        mBinding.textviewNewsTitle.setText(news.getTitle());
        mBinding.textviewNewsContent.setText(news.getContent());

        mBinding.buttonNewsUrl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl())));
            }
        });

        return view;
    }
}
