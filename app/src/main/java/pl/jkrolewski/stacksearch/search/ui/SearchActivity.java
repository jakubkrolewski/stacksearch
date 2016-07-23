package pl.jkrolewski.stacksearch.search.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.R;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponentProvider;
import pl.jkrolewski.stacksearch.search.SearchModule;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import pl.jkrolewski.stacksearch.search.ui.results.ResultsView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchActivity extends RxAppCompatActivity {

    @Inject
    SearchNetworkService searchNetworkService;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.results)
    ResultsView resultsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();
        setupView();
    }

    private void injectDependencies() {
        ApplicationComponentProvider.<ApplicationComponent>fromContext(this)
                .plus(new SearchModule())
                .inject(this);
    }

    private void setupView() {
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        setupSearchView(searchView);
        return true;
    }

    private void setupSearchView(@NonNull SearchView searchView) {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void loadResults(@NonNull String query) {
        searchNetworkService.findQuestions(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        this::handleSearchResponse,
                        this::handleSearchError
                );
    }

    private void handleSearchResponse(@NonNull SearchResponse response) {
        resultsView.setQuestions(response.getItems());
    }

    private void handleSearchError(Throwable error) {
        Timber.w(error, "Search error");
        Toast.makeText(this, R.string.search_error, Toast.LENGTH_SHORT).show();
    }
}
