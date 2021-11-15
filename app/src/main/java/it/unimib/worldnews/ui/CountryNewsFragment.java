package it.unimib.worldnews.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.unimib.worldnews.R;
import it.unimib.worldnews.adapter.NewsListViewArrayAdapter;
import it.unimib.worldnews.adapter.NewsListViewBaseAdapter;
import it.unimib.worldnews.adapter.NewsRecyclerViewAdapter;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.model.NewsSource;

/**
 * It shows the news by country.
 */
public class CountryNewsFragment extends Fragment {

    private static final String TAG = "CountryNewsFragment";

    private ListView mListViewCountryNews;
    private News[] mNewsArray;
    private List<News> mNewsList;

    private RecyclerView mReciclerViewCountryNews;

    public CountryNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsArray = new News[20];
        mNewsList = new ArrayList<>();

        for (int i = 0; i < mNewsArray.length; i++) {
            mNewsArray[i] = new News(new NewsSource("Source"), null,
                    "Title " + i, null, null, null, null, null);
            mNewsList.add(new News(new NewsSource("Source"), null,
                    "Title " + i, null, null, null, null, null));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country_news_recyclerview, container, false);

        mReciclerViewCountryNews = view.findViewById(R.id.recyclerview_country_news);

        /*NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(mNewsList,
                new NewsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(News news) {
                        Log.d(TAG, "onItemClick: " + news);
                    }
                });
        mReciclerViewCountryNews.setLayoutManager(new LinearLayoutManager(getContext()));
        mReciclerViewCountryNews.setAdapter(adapter);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_country_news_listview, container, false);

        mListViewCountryNews = view.findViewById(R.id.listview_country_news);*/

        /*ArrayAdapter<News> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mNewsArray);

        mListViewCountryNews.setAdapter(adapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        /*NewsListViewArrayAdapter newsNewsListViewArrayAdapter =
                new NewsListViewArrayAdapter(getContext(), R.layout.country_news_list_item, mNewsArray);

        mListViewCountryNews.setAdapter(newsNewsListViewArrayAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        /*NewsListViewBaseAdapter newsListViewBaseAdapter = new NewsListViewBaseAdapter(requireActivity(), mNewsList);

        mListViewCountryNews.setAdapter(newsListViewBaseAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + mNewsList.get(position));
            }
        });*/

        mReciclerViewCountryNews = view.findViewById(R.id.recyclerview_country_news);

        mReciclerViewCountryNews.setLayoutManager(new LinearLayoutManager(getContext()));

        NewsResponse newsResponseWithJsonReader = readJsonFileWithJsonReader();
        NewsResponse newsResponseWithGson = readJsonFileWithGson();

        List<News> newsListWithJsonReader = newsResponseWithJsonReader.getArticles();
        List<News> newsListWithGson = newsResponseWithGson.getArticles();

        for (int i = 0; i < newsListWithJsonReader.size(); i++) {
            Log.d(TAG, "JsonReader: " + newsListWithJsonReader.get(i));
        }

        for (int i = 0; i < newsListWithGson.size(); i++) {
            Log.d(TAG, "Gson: " + newsListWithGson.get(i));
        }

        NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(newsListWithGson,
                new NewsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(News news) {
                        Log.d(TAG, "onItemClick: " + news);
                    }
                });
        mReciclerViewCountryNews.setAdapter(adapter);

        return view;
    }

    private NewsResponse readJsonFileWithJsonReader() {

        NewsResponse newsResponse = new NewsResponse();
        List<News> newsList = new ArrayList<>();

        try {
            InputStream fileInputStream = getActivity().getAssets().open("news-test.json");

            JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));

            jsonReader.beginObject(); // root

            while (jsonReader.hasNext()) {
                String responseParameter = jsonReader.nextName();

                if (responseParameter.equals("status")) {
                    newsResponse.setStatus(jsonReader.nextString());
                } else if (responseParameter.equals("totalResults")) {
                    newsResponse.setTotalResults(jsonReader.nextInt());
                } else if (responseParameter.equals("articles")) {
                    jsonReader.beginArray(); // articles array

                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject(); // article object

                        News news = new News();

                        while (jsonReader.hasNext()) {

                            String articleParameter = jsonReader.nextName();

                            if (articleParameter.equals("title") && jsonReader.peek() != JsonToken.NULL) {
                                String title = jsonReader.nextString();
                                Log.d(TAG, "readJsonFile: " + title);
                                news.setTitle(title);
                            } else if (articleParameter.equals("author") && jsonReader.peek() != JsonToken.NULL) {
                                news.setAuthor(jsonReader.nextString());
                            } else if (articleParameter.equals("description") && jsonReader.peek() != JsonToken.NULL) {
                                news.setDescription(jsonReader.nextString());
                            } else if (articleParameter.equals("url") && jsonReader.peek() != JsonToken.NULL) {
                                news.setUrl(jsonReader.nextString());
                            } else if (articleParameter.equals("urlToImage") && jsonReader.peek() != JsonToken.NULL) {
                                news.setUrlToImage(jsonReader.nextString());
                            } else if (articleParameter.equals("publishedAt") && jsonReader.peek() != JsonToken.NULL) {
                                news.setPublishedAt(jsonReader.nextString());
                            } else if (articleParameter.equals("content") && jsonReader.peek() != JsonToken.NULL) {
                                news.setContent(jsonReader.nextString());
                            } else if (articleParameter.equals("source") && jsonReader.peek() != JsonToken.NULL) {
                                jsonReader.beginObject(); // source object

                                while (jsonReader.hasNext()) {
                                    String sourceParameter = jsonReader.nextName();
                                    if (sourceParameter.equals("name") && jsonReader.peek() != JsonToken.NULL) {
                                        String source = jsonReader.nextString();
                                        Log.d(TAG, "readJsonFile source: " + source);
                                        news.setNewsSource(new NewsSource(source));
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject(); // end source object
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject(); // end article object
                        newsList.add(news);
                    }

                    jsonReader.endArray(); // end articles array
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject(); // end root
        } catch (IOException e) {
            e.printStackTrace();
        }
        newsResponse.setArticles(newsList);
        return newsResponse;
    }

    private NewsResponse readJsonFileWithGson() {

        NewsResponse newsResponse = null;

        try {
            InputStream inputStream = getActivity().getAssets().open("news-test.json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            newsResponse = new Gson().fromJson(bufferedReader, NewsResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsResponse;
    }
}
