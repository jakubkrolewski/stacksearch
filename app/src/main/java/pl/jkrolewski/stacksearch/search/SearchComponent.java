package pl.jkrolewski.stacksearch.search;


import dagger.Subcomponent;
import pl.jkrolewski.stacksearch.base.dagger.ActivityScope;
import pl.jkrolewski.stacksearch.search.ui.SearchPresenter;

@ActivityScope
@Subcomponent(modules = {
        SearchModule.class
})
public interface SearchComponent {

    void inject(SearchPresenter searchPresenter);
}
