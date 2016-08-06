package pl.jkrolewski.stacksearch.search.database;

import android.database.sqlite.SQLiteDatabase;

import com.googlecode.catchexception.throwable.apis.BDDCatchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsTableTest {

    private SearchResultsTable table = new SearchResultsTable();

    @Test
    public void shouldCreateTableWithoutException() {
        // given
        SQLiteDatabase db = SQLiteDatabase.create(null);

        // when
        BDDCatchThrowable.when(() -> table.create(db));

        // then
        BDDCatchThrowable.thenCaughtThrowable().isNull();
    }
}