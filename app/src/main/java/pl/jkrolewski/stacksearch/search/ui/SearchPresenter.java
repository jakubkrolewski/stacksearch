package pl.jkrolewski.stacksearch.search.ui;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import lombok.NonNull;
import nucleus.presenter.RxPresenter;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponentProvider;
import pl.jkrolewski.stacksearch.search.SearchModule;
import pl.jkrolewski.stacksearch.search.database.SearchDatabaseService;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchPresenter extends RxPresenter<SearchActivity> {

    private static final int REQUEST_DATABASE_SEARCH_RESULTS = 1;
    private static final int REQUEST_UPDATE_DATABASE_ONLINE = 2;

    private static final String KEY_QUERY = "query";

    @Inject
    SearchNetworkService searchNetworkService;

    @Inject
    SearchDatabaseService searchDatabaseService;

    private String query;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        injectDependencies();
        restoreState(savedState);
        setupDatabaseRestartable();
        setupOnlineRestartable();
    }

    private void injectDependencies() {
        ApplicationComponentProvider.get()
                .plus(new SearchModule())
                .inject(this);
    }

    private void restoreState(@Nullable Bundle savedState) {
        if (savedState != null) {
            query = savedState.getString(KEY_QUERY);
        }
    }

    private void setupDatabaseRestartable() {
        restartableLatestCache(REQUEST_DATABASE_SEARCH_RESULTS,
                this::createLoadQuestionsFromDatabaseObservable,
                SearchActivity::handleSearchResponse);
    }

    private void setupOnlineRestartable() {
        restartableFirst(REQUEST_UPDATE_DATABASE_ONLINE,
                this::createFindQuestionsOnlineAndUpdateDatabaseObservable,
                (searchActivity, ignored) -> Timber.d("Database updated"),
                SearchActivity::handleSearchError);
    }

    @NonNull
    @CheckResult
    private Observable<SearchResponse> createLoadQuestionsFromDatabaseObservable() {
        return searchDatabaseService.findQuestions(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @CheckResult
    private Observable<Long> createFindQuestionsOnlineAndUpdateDatabaseObservable() {
        String queryToUse = query;

        return searchNetworkService.findQuestions(queryToUse)
                .flatMap(searchResponse -> searchDatabaseService.addOrReplaceQuestions(queryToUse, searchResponse).toObservable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onSave(@NonNull Bundle state) {
        super.onSave(state);
        state.putString(KEY_QUERY, query);
    }

    public void executeSearch(@NonNull String query) {
        this.query = query;

        stop(REQUEST_DATABASE_SEARCH_RESULTS);
        start(REQUEST_DATABASE_SEARCH_RESULTS);
        start(REQUEST_UPDATE_DATABASE_ONLINE);
    }

    public String getLastQuery() {
        return query;
    }
}
