package pl.jkrolewski.stacksearch.search.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NonNull;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import pl.jkrolewski.stacksearch.R;
import pl.jkrolewski.stacksearch.details.SimpleWebViewActivity;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.ui.results.ResultsView;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiresPresenter(SearchPresenter.class)
public class SearchActivity extends NucleusAppCompatActivity<SearchPresenter> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.results)
    ResultsView resultsView;

    @VisibleForTesting
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
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
        resultsView.setOnItemClickListener((view, item) -> openLink(item.getLink()));

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
        String lastQuery = getPresenter().getLastQuery();
        searchView.setQuery(lastQuery, false);
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

        if (!TextUtils.isEmpty(lastQuery)) {
            enabledSwipeRefresh();
        }
    }

    private void loadResults(@NonNull String query) {
        disableSwipeRefresh();
        resultsView.setRefreshing(true);

        getPresenter().executeSearch(query);
    }

    private void disableSwipeRefresh() {
        resultsView.setSwipeRefreshEnabled(false);
    }

    void handleSearchResponse(@NonNull SearchResponse response) {
        resultsView.setQuestions(response.getItems());
        enabledSwipeRefresh();
    }

    void handleSearchError(@NonNull Throwable error) {
        Timber.w(error, "Search error");
        resultsView.notifyRefreshError();
        enabledSwipeRefresh();
    }

    private void enabledSwipeRefresh() {
        resultsView.setSwipeRefreshEnabled(true);
    }
}
