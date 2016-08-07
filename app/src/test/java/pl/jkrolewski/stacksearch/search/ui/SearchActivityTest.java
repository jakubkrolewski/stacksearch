package pl.jkrolewski.stacksearch.search.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import pl.jkrolewski.stacksearch.search.model.SearchResponseFactory;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(RobolectricTestRunner.class)
public class SearchActivityTest {

    private ActivityController<? extends SearchActivity> activityController;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(SearchActivityWithMockPresenter.class);
    }

    @Test
    public void shouldHaveSwipeRefreshDisabledWhenCreatedWithEmptyQuery() {
        // when
        activityController.create();

        // then
        assertThat(activityController.get().resultsView).isDisabled();
    }

    @Test
    public void shouldHaveSwipeRefreshEnabledWhenCreatedWithNotEmptyQuery() {
        // given
        given(activityController.get().getPresenter().getLastQuery())
                .willReturn("some query");

        // when
        activityController.create().start().resume().visible();

        // then
        assertThat(activityController.get().resultsView).isEnabled();
    }

    @Test
    public void shouldHaveSwipeRefreshEnabledWhenSearchEndsWithSuccess() {
        // given
        activityController.create();

        // when
        activityController.get().handleSearchResponse(SearchResponseFactory.create());

        // then
        assertThat(activityController.get().resultsView).isEnabled();
    }

    @Test
    public void shouldHaveSwipeRefreshEnabledWhenSearchEndsWithError() {
        // given
        activityController.create();

        // when
        activityController.get().handleSearchError(new Exception());

        // then
        assertThat(activityController.get().resultsView).isEnabled();
    }

    @Test
    public void shouldHaveSwipeRefreshDisabledWhenSearchInProgress() {
        // given
        activityController.create().start().resume().visible();

        // when
        activityController.get().searchView.setQuery("test", true);

        // then
        assertThat(activityController.get().resultsView).isDisabled();
    }

    private static class SearchActivityWithMockPresenter extends SearchActivity {

        @Mock
        private SearchPresenter presenter;

        public SearchActivityWithMockPresenter() {
            MockitoAnnotations.initMocks(this);
        }

        @Override
        public SearchPresenter getPresenter() {
            return presenter;
        }
    }
}