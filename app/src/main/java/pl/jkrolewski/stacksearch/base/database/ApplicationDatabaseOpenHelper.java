package pl.jkrolewski.stacksearch.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;

import java.util.Collections;
import java.util.List;

import pl.jkrolewski.stacksearch.search.database.SearchResultsTable;

@VisibleForTesting
public class ApplicationDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database";

    private static final int DATABASE_VERSION = 1;

    private final List<Table> tables = Collections.singletonList(
            new SearchResultsTable()
    );

    public ApplicationDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (Table table : tables) {
            table.create(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // nop
    }
}