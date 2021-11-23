package it.unimib.worldnews.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.unimib.worldnews.model.News;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    List<News> getAll();

    @Insert
    void insertNewsList(List<News> newsList);

    @Insert
    void insertAll(News... news);

    @Delete
    void delete(News news);

    @Query("DELETE FROM news")
    void deleteAll();

    @Delete
    void deleteAllWithoutQuery(News... news);
}
