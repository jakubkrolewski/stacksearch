package pl.jkrolewski.stacksearch.search.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.R;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponentProvider;
import pl.jkrolewski.stacksearch.search.SearchModule;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchActivity extends RxAppCompatActivity {

    @Inject
    SearchNetworkService searchNetworkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();
        loadData();
    }

    private void injectDependencies() {
        ApplicationComponentProvider.<ApplicationComponent>fromContext(this)
                .plus(new SearchModule())
                .inject(this);
    }

    private void loadData() {
        searchNetworkService.findQuestions("test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        this::handleSearchResponse,
                        this::handleSearchError
                );
    }

    private void handleSearchResponse(@NonNull SearchResponse response) {
        Timber.d("Search response: %s", response);
    }

    private void handleSearchError(Throwable error) {
        Timber.w(error, "Search error");
        Toast.makeText(this, R.string.search_error, Toast.LENGTH_SHORT).show();
    }
}
