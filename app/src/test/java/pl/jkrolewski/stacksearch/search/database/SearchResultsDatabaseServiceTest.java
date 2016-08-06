package pl.jkrolewski.stacksearch.search.database;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import pl.jkrolewski.stacksearch.base.database.ApplicationDatabaseOpenHelper;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.model.SearchResponseFactory;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SearchResultsDatabaseServiceTest {

    private SearchResultsDatabaseService databaseService;

    @Before
    public void setUp() {
        SqlBrite sqlBrite = SqlBrite.create();
        BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(
                new ApplicationDatabaseOpenHelper(RuntimeEnvironment.application), Schedulers.immediate()
        );
        JsonAdapter<SearchResponse> jsonAdapter = new Moshi.Builder().build().adapter(SearchResponse.class);
        databaseService = new SearchResultsDatabaseService(briteDatabase, jsonAdapter);
    }

    @Test
    public void shouldAddSearchResponse() {
        // given
        String query = "some query";
        SearchResponse insertedResponse = SearchResponseFactory.create();

        // when
        databaseService.addOrReplaceQuestions(query, insertedResponse).toBlocking().value();

        // then
        SearchResponse readResponse = databaseService.findQuestions(query).toBlocking().first();
        assertThat(readResponse).isEqualTo(insertedResponse);
    }

    @Test
    public void shouldReplaceSearchResponse() {
        // given
        String query = "some query";
        SearchResponse oldResponse = SearchResponseFactory.create(0);

        databaseService.addOrReplaceQuestions(query, oldResponse).toBlocking().value();

        SearchResponse newResponse = SearchResponseFactory.create(1);

        // when
        databaseService.addOrReplaceQuestions(query, newResponse).toBlocking().value();

        // then
        SearchResponse readResponse = databaseService.findQuestions(query).toBlocking().first();
        assertThat(readResponse).isEqualTo(newResponse);
    }
}