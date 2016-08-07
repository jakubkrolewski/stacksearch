package pl.jkrolewski.stacksearch.search.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.TestStackSearchApplication;
import pl.jkrolewski.stacksearch.search.SearchComponent;
import pl.jkrolewski.stacksearch.search.database.SearchDatabaseService;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.model.SearchResponseFactory;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import rx.Observable;
import rx.Single;
import rx.subjects.ReplaySubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class SearchPresenterTest {

    @Mock
    private SearchComponent searchComponent;

    @Mock
    private SearchNetworkService networkService;

    @Mock
    private SearchDatabaseService databaseService;

    @Mock
    private SearchActivity activity;

    private SearchPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestStackSearchApplication.getInstance().setSearchComponent(searchComponent);

        willAnswer(invocation -> {
            SearchPresenter presenter = (SearchPresenter) invocation.getArguments()[0];
            presenter.searchNetworkService = networkService;
            presenter.searchDatabaseService = databaseService;
            return null;
        }).given(searchComponent).inject(any(SearchPresenter.class));

        presenter = new SearchPresenter();
        presenter.onCreate(null);
        presenter.takeView(activity);
    }

    @Test
    public void shouldDeliverSearchResultsFromDatabaseWhenRequested() {
        // given
        SearchResponse searchResponse = SearchResponseFactory.create();
        given(databaseService.findQuestions("some query"))
                .willReturn(Observable.just(searchResponse));
        given(networkService.findQuestions("some query"))
                .willReturn(Observable.never());

        // when
        presenter.executeSearch("some query");

        // then
        verify(activity).handleSearchResponse(searchResponse);
    }

    @Test
    public void shouldDeliverSearchResultsFromNetworkWhenDatabaseInitiallyEmptyAndUpdated() {
        // given
        String query = "some query";
        SearchResponse searchResponse = SearchResponseFactory.create();
        mockDatabaseServiceWithSubject(query);

        given(networkService.findQuestions(query))
                .willReturn(Observable.just(searchResponse));

        // when
        presenter.executeSearch(query);

        // then
        verify(activity).handleSearchResponse(searchResponse);
    }

    @Test
    public void shouldDeliverSearchResultsFromBothNetworkAndDatabaseWhenDatabaseHasInitialValueAndUpdated() {
        // given
        String query = "some query";
        SearchResponse initialDbContent = SearchResponseFactory.create(0);
        SearchResponse networkResponse = SearchResponseFactory.create(1);
        mockDatabaseServiceWithSubject(query);

        databaseService.addOrReplaceQuestions(query, initialDbContent).toBlocking().value();

        given(networkService.findQuestions(query))
                .willReturn(Observable.just(networkResponse));

        // when
        presenter.executeSearch(query);

        // then
        ArgumentCaptor<SearchResponse> argumentCaptor = ArgumentCaptor.forClass(SearchResponse.class);

        verify(activity, times(2)).handleSearchResponse(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues()).containsExactly(initialDbContent, networkResponse);
    }

    @Test
    public void shouldDeliverErrorNotificationWhenUpdatingDatabaseFails() {
        // given
        String query = "some query";
        IOException networkException = new IOException();

        mockDatabaseServiceWithSubject(query);

        given(networkService.findQuestions(query))
                .willReturn(Observable.error(networkException));

        // when
        presenter.executeSearch(query);

        // then
        verify(activity).handleSearchError(networkException);
    }

    private void mockDatabaseServiceWithSubject(@NonNull String query) {
        ReplaySubject<SearchResponse> fakeDatabase = ReplaySubject.create();

        given(databaseService.findQuestions(query))
                .willReturn(fakeDatabase);

        given(databaseService.addOrReplaceQuestions(eq(query), isA(SearchResponse.class))).will(invocation -> {
            SearchResponse searchResponse = (SearchResponse) invocation.getArguments()[1];
            fakeDatabase.onNext(searchResponse);
            return Single.just(0L);
        });
    }
}