package pl.jkrolewski.stacksearch.base;


import com.google.common.base.Preconditions;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.rxhooks.TestRxJavaSchedulersHookManager;
import pl.jkrolewski.stacksearch.search.SearchComponent;
import pl.jkrolewski.stacksearch.search.SearchModule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class TestStackSearchApplication extends StackSearchApplication implements TestLifecycleApplication {

    private ApplicationComponent appComponent;
    private SearchComponent searchComponent = mock(SearchComponent.class);

    @NonNull
    public static TestStackSearchApplication getInstance() {
        return (TestStackSearchApplication) RuntimeEnvironment.application;
    }

    @NonNull
    @Override
    public ApplicationComponent getComponent() {
        if (appComponent == null) {
            createApplicationComponent();
        }

        return appComponent;
    }

    private void createApplicationComponent() {
        appComponent = mock(ApplicationComponent.class);
        given(appComponent.plus(any(SearchModule.class)))
                .willReturn(searchComponent);
    }

    public void setSearchComponent(@NonNull SearchComponent searchComponent) {
        Preconditions.checkState(appComponent == null, "Graph already created");
        this.searchComponent = searchComponent;
    }

    @Override
    public void beforeTest(Method method) {
        TestRxJavaSchedulersHookManager.installImmediateScheduler();
    }

    @Override
    public void prepareTest(Object test) {
        TestRxJavaSchedulersHookManager.installImmediateScheduler();
    }

    @Override
    public void afterTest(Method method) {
        // nop
    }
}