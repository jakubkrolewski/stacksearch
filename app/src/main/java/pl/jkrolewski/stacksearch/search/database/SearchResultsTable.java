package pl.jkrolewski.stacksearch.search.database;

import android.database.sqlite.SQLiteDatabase;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.database.Table;

public class SearchResultsTable implements Table {

    public static final String TABLE_NAME = "search_results";

    @Override
    public void create(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + Columns.QUERY + " TEXT PRIMARY KEY, "
                + Columns.RESULTS_JSON + " TEXT NOT NULL" +
                ")");
    }

    public static class Columns {
        public static final String QUERY = "query";
        public static final String RESULTS_JSON = "results_json";
    }
}
