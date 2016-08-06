package pl.jkrolewski.stacksearch.search;

import com.squareup.moshi.Moshi;
import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.dagger.ActivityScope;
import pl.jkrolewski.stacksearch.search.database.SearchDatabaseService;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import retrofit2.Retrofit;

@Module
public class SearchModule {

    @Provides
    @ActivityScope
    public SearchNetworkService provideSearchNetworkService(@NonNull Retrofit retrofit) {
        return retrofit.create(SearchNetworkService.class);
    }

    @Provides
    @ActivityScope
    public SearchDatabaseService provideSearchResultsDatabaseService(@NonNull BriteDatabase database, @NonNull Moshi moshi) {
        return new SearchDatabaseService(database, moshi.adapter(SearchResponse.class));
    }
}
