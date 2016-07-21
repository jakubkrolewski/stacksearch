package pl.jkrolewski.stacksearch.base;

import android.app.Application;
import android.support.annotation.NonNull;

import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.DaggerApplicationComponent;
import pl.jkrolewski.stacksearch.base.network.NetworkModule;
import timber.log.Timber;

public class StackSearchApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

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
