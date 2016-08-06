package pl.jkrolewski.stacksearch.search.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.sqlbrite.BriteDatabase;

import java.io.IOException;

import lombok.SneakyThrows;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import rx.Observable;
import rx.Single;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class SearchResultsDatabaseService {

    private final BriteDatabase database;
    private final JsonAdapter<SearchResponse> searchResponseJsonAdapter;

    public SearchResultsDatabaseService(@NonNull BriteDatabase database,
                                        @NonNull JsonAdapter<SearchResponse> searchResponseJsonAdapter) {
        this.database = database;
        this.searchResponseJsonAdapter = searchResponseJsonAdapter;
    }

    @NonNull
    @CheckResult
    public Observable<SearchResponse> findQuestions(@NonNull String query) {
        return database.createQuery(SearchResultsTable.TABLE_NAME, "SELECT " + SearchResultsTable.Columns.RESULTS_JSON
                + " FROM " + SearchResultsTable.TABLE_NAME
                + " WHERE " + SearchResultsTable.Columns.QUERY + "=?", query)
                .mapToOne(this::deserializeSearchResponse);
    }

    @NonNull
    @CheckResult
    public Single<Long> addOrReplaceQuestions(@NonNull String query, @NonNull SearchResponse searchResponse) {
        return Single.fromCallable(() ->
                database.insert(SearchResultsTable.TABLE_NAME, serializeQueryWithResponse(query, searchResponse), CONFLICT_REPLACE)
        );
    }

    @SneakyThrows(IOException.class)
    @NonNull
    private SearchResponse deserializeSearchResponse(@NonNull Cursor cursor) {
        String resultsJson = cursor.getString(0);

        return searchResponseJsonAdapter.fromJson(resultsJson);
    }

    @NonNull
    private ContentValues serializeQueryWithResponse(@NonNull String query, @NonNull SearchResponse searchResponse) {
        ContentValues values = new ContentValues();
        values.put(SearchResultsTable.Columns.QUERY, query);
        values.put(SearchResultsTable.Columns.RESULTS_JSON, searchResponseJsonAdapter.toJson(searchResponse));
        return values;
    }
}
