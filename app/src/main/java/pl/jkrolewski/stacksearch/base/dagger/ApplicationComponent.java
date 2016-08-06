package pl.jkrolewski.stacksearch.base.dagger;


import javax.inject.Singleton;

import dagger.Component;
import pl.jkrolewski.stacksearch.base.database.DatabaseModule;
import pl.jkrolewski.stacksearch.base.network.NetworkModule;
import pl.jkrolewski.stacksearch.search.SearchComponent;
import pl.jkrolewski.stacksearch.search.SearchModule;

@Singleton
@Component(modules = {
        NetworkModule.class,
        DatabaseModule.class
})
public interface ApplicationComponent {

    SearchComponent plus(SearchModule module);
}
