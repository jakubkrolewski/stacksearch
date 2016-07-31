package pl.jkrolewski.stacksearch.search.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.R;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponentProvider;
import pl.jkrolewski.stacksearch.details.SimpleWebViewActivity;
import pl.jkrolewski.stacksearch.search.SearchModule;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import pl.jkrolewski.stacksearch.search.ui.results.ResultsView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchActivity extends RxAppCompatActivity {

    @Inject
    SearchNetworkService searchNetworkService;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.results)
    ResultsView resultsView;

    private SearchView searchView;

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
        setUpToolbar();
        setupResultsView();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = checkNotNull(getSupportActionBar());

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
    }

    private void setupResultsView() {
        resultsView.setOnItemClickListener((view, item) -> {
            openLink(item.getLink());
        });

        disableSwipeRefresh();
        resultsView.setOnRefreshListener(() -> loadResults(searchView.getQuery().toString()));
    }

    private void openLink(@NonNull String link) {
        startActivity(SimpleWebViewActivity.createIntent(this, link));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        setupSearchView(searchView);
        return true;
    }

    private void setupSearchView(@NonNull SearchView searchView) {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
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
        disableSwipeRefresh();

        searchNetworkService.findQuestions(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        this::handleSearchResponse,
                        this::handleSearchError
                );
    }

    private void disableSwipeRefresh() {
        resultsView.setSwipeRefreshEnabled(false);
    }

    private void handleSearchResponse(@NonNull SearchResponse response) {
        resultsView.setQuestions(response.getItems());
        enabledSwipeRefresh();
    }

    private void handleSearchError(@NonNull Throwable error) {
        Timber.w(error, "Search error");
        resultsView.notifyRefreshError();
        enabledSwipeRefresh();
    }

    private void enabledSwipeRefresh() {
        resultsView.setSwipeRefreshEnabled(true);
    }
}
