package pl.jkrolewski.stacksearch.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.DaggerApplicationComponent;
import pl.jkrolewski.stacksearch.base.network.NetworkModule;
import timber.log.Timber;

public class StackSearchApplication extends Application {

    private static StackSearchApplication instance;
    private ApplicationComponent applicationComponent;

    @NonNull
    public static StackSearchApplication getInstance() {
        Preconditions.checkState(instance != null, "You can't call it before onCreate()");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .build();

        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
