package it.unimib.worldnews.repository;

import android.app.Application;
import android.util.JsonReader;
import android.util.JsonToken;

import androidx.lifecycle.MutableLiveData;

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

import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsResponse;
import it.unimib.worldnews.model.NewsSource;

public class NewsMockRepositoryWithLiveData implements INewsRepositoryWithLiveData {

    private static final String NEWS_JSON_FILE = "news-test.json";

    private final Application mApplication;
    private final INewsRepositoryWithLiveData.JsonParser mJsonParser;
    private MutableLiveData<NewsResponse> mNewsResponseLiveData;

    public NewsMockRepositoryWithLiveData(Application application, INewsRepositoryWithLiveData.JsonParser jsonParser) {
        this.mApplication = application;
        this.mJsonParser = jsonParser;
        this.mNewsResponseLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<NewsResponse> fetchNews(String country, int page, long lastUpdate) {
        NewsResponse newsResponse = null;

        switch (mJsonParser) {
            case JSON_READER:
                newsResponse = readJsonFileWithJsonReader();
                break;
            case JSON_OBJECT_ARRAY:
                newsResponse = readJsonFileWithJsonObjectArray();
                break;
            case GSON:
                newsResponse = readJsonFileWithGson();
                break;
            case JSON_ERROR:
                break;
        }

        mNewsResponseLiveData.postValue(newsResponse);

        return mNewsResponseLiveData;
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
            InputStream fileInputStream = getInputStreamFromFile(NEWS_JSON_FILE);

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
            InputStream fileInputStream = getInputStreamFromFile(NEWS_JSON_FILE);

            if (fileInputStream != null) {
                int size = fileInputStream.available();

                // Read the entire asset into a local byte buffer.
                byte[] buffer = new byte[size];
                if (fileInputStream.read(buffer) > 0) {

                    fileInputStream.close();

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
                }
            }
        } catch(IOException | JSONException e){
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
        InputStream inputStream = getInputStreamFromFile(NEWS_JSON_FILE);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, NewsResponse.class);
    }

    /**
     * It gets the InputStream from a file contained in the "assets" folder.
     * @param fileName The name of the file to read.
     * @return The InputStream associated with the content of the file.
     */
    private InputStream getInputStreamFromFile(String fileName) {
        try {
            return mApplication.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
