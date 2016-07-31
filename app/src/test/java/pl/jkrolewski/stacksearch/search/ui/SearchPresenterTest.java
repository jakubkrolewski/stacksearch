package pl.jkrolewski.stacksearch.search.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import pl.jkrolewski.stacksearch.base.TestStackSearchApplication;
import pl.jkrolewski.stacksearch.rxhooks.TestRxJavaSchedulersHookManager;
import pl.jkrolewski.stacksearch.search.SearchComponent;
import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import pl.jkrolewski.stacksearch.search.model.SearchResponseFactory;
import pl.jkrolewski.stacksearch.search.network.SearchNetworkService;
import rx.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class SearchPresenterTest {

    @Mock
    private SearchComponent searchComponent;

    @Mock
    private SearchNetworkService searchNetworkService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestStackSearchApplication.getInstance().setSearchComponent(searchComponent);

        willAnswer(invocation -> {
            SearchPresenter presenter = (SearchPresenter) invocation.getArguments()[0];
            presenter.searchNetworkService = searchNetworkService;
            return null;
        }).given(searchComponent).inject(any(SearchPresenter.class));

        TestRxJavaSchedulersHookManager.installImmediateScheduler();
    }

    @Test
    public void shouldDeliverSearchResultsWhenRequested() {
        // given
        SearchResponse searchResponse = SearchResponseFactory.create();
        given(searchNetworkService.findQuestions("some query"))
                .willReturn(Observable.just(searchResponse));
        SearchPresenter presenter = new SearchPresenter();
        SearchActivity activity = mock(SearchActivity.class);
        presenter.onCreate(null);
        presenter.takeView(activity);

        // when
        presenter.executeSearch("some query");

        // then
        verify(activity).handleSearchResponse(searchResponse);
    }
}