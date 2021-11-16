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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
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

    private News[] mNewsArray;
    private List<News> mNewsList;

    private List<News> mNewsListWithJsonReader;
    private List<News> mNewsListWithJsonObjectArray;
    private List<News> mNewsListWithGson;

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

        NewsResponse newsResponseWithJsonReader = readJsonFileWithJsonReader();
        NewsResponse newsResponseWithJsonObjectArray = readJsonFileWithJsonObjectArray();
        NewsResponse newsResponseWithGson = readJsonFileWithGson();

        mNewsListWithJsonReader = newsResponseWithJsonReader.getArticles();
        mNewsListWithJsonObjectArray = newsResponseWithJsonObjectArray.getArticles();
        mNewsListWithGson = newsResponseWithGson.getArticles();

        for (int i = 0; i < mNewsListWithJsonReader.size(); i++) {
            Log.d(TAG, "JsonReader: " + mNewsListWithJsonReader.get(i));
        }

        for (int i = 0; i < mNewsListWithJsonObjectArray.size(); i++) {
            Log.d(TAG, "JsonObjectArray: " + mNewsListWithJsonObjectArray.get(i));
        }

        for (int i = 0; i < mNewsListWithGson.size(); i++) {
            Log.d(TAG, "Gson: " + mNewsListWithGson.get(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country_news_recyclerview, container, false);

        RecyclerView mRecyclerViewCountryNews = view.findViewById(R.id.recyclerview_country_news);
        mRecyclerViewCountryNews.setLayoutManager(new LinearLayoutManager(getContext()));

        NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(mNewsListWithJsonObjectArray,
                new NewsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(News news) {
                        Log.d(TAG, "onItemClick: " + news);
                    }
                });
        mRecyclerViewCountryNews.setAdapter(adapter);

        /* ###### Examples with ListView and different types of Adapters ###### */

        // Inflate the layout for this fragment
        /*View view = inflater.inflate(R.layout.fragment_country_news_listview, container, false);

        ListView mListViewCountryNews = view.findViewById(R.id.listview_country_news);*/

        // ArrayAdapter

        /*ArrayAdapter<News> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mNewsArray);

        mListViewCountryNews.setAdapter(adapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        // Custom Adapter that extends ArrayAdapter

        /*NewsListViewArrayAdapter newsNewsListViewArrayAdapter =
                new NewsListViewArrayAdapter(requireContext(), R.layout.country_news_list_item, mNewsArray);

        mListViewCountryNews.setAdapter(newsNewsListViewArrayAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + mNewsArray[i]);
            }
        });*/

        // Custom Adapter that extends Base

        /*NewsListViewBaseAdapter newsListViewBaseAdapter =
                new NewsListViewBaseAdapter(mNewsListWithGson);

        mListViewCountryNews.setAdapter(newsListViewBaseAdapter);

        mListViewCountryNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + mNewsListWithGson.get(position));
            }
        });*/

        return view;
    }

    /**
     * It parses the news-test.json file using the JsonReader
     * (https://developer.android.com/reference/android/util/JsonReader) class.
     *
     * @return The NewsResponse object associated with the parsed JSON file.
     */
    private NewsResponse readJsonFileWithJsonReader() {

        NewsResponse newsResponse = new NewsResponse();
        List<News> newsList = new ArrayList<>();

        try {
            InputStream fileInputStream = getActivity().getAssets().open("news-test.json");

            JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));

            jsonReader.beginObject(); // root

            while (jsonReader.hasNext()) {
                String responseParameter = jsonReader.nextName();

                if (responseParameter.equals("status") && jsonReader.peek() != JsonToken.NULL) {
                    newsResponse.setStatus(jsonReader.nextString());
                } else if (responseParameter.equals("totalResults") && jsonReader.peek() != JsonToken.NULL) {
                    newsResponse.setTotalResults(jsonReader.nextInt());
                } else if (responseParameter.equals("articles") && jsonReader.peek() != JsonToken.NULL) {
                    jsonReader.beginArray(); // articles array

                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject(); // article object

                        News news = new News();

                        while (jsonReader.hasNext()) {

                            String articleParameter = jsonReader.nextName();

                            if (articleParameter.equals("title") && jsonReader.peek() != JsonToken.NULL) {
                                String title = jsonReader.nextString();
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

    /**
     * It parses the news-test.json file using JSONObject (https://developer.android.com/reference/org/json/JSONObject)
     * ans JSONArray (https://developer.android.com/reference/org/json/JSONArray) classes.
     *
     * @return The NewsResponse object associated with the parsed JSON file.
     */
    private NewsResponse readJsonFileWithJsonObjectArray() {

        NewsResponse newsResponse = null;

        try {

            InputStream is = getActivity().getAssets().open("news-test.json");

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonContent = new String(buffer);

            newsResponse = new NewsResponse();
            JSONObject rootJsonObject = new JSONObject(jsonContent);

            newsResponse.setStatus(rootJsonObject.getString("status"));
            newsResponse.setTotalResults(rootJsonObject.getInt("totalResults"));

            JSONArray articlesJsonArray = rootJsonObject.getJSONArray("articles");
            List<News> articlesList = new ArrayList<>();
            newsResponse.setArticles(articlesList);

            for (int i = 0; i < articlesJsonArray.length(); i++) {

                JSONObject newsJsonObject = articlesJsonArray.getJSONObject(i);

                NewsSource newsSource = new NewsSource(newsJsonObject.getJSONObject("source").getString("name"));

                articlesList.add(new News(
                        newsSource,
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("publishedAt"),
                        newsJsonObject.getString("content")
                ));
            }

        } catch (IOException | JSONException e) {
                e.printStackTrace();
        }

        return newsResponse;
    }

    /**
     * It parses the news-test.json file using Gson (https://github.com/google/gson)
     *
     * @return The NewsResponse object associated with the parsed JSON file.
     */
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
