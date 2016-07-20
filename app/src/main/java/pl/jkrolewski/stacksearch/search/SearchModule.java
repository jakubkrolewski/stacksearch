package pl.jkrolewski.stacksearch.search;

import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.dagger.ActivityScope;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import retrofit2.Retrofit;

@Module
public class SearchModule {

    @Provides
    @ActivityScope
    public SearchNetworkService getSearchNetworkService(@NonNull Retrofit retrofit) {
        return retrofit.create(SearchNetworkService.class);
    }
}
