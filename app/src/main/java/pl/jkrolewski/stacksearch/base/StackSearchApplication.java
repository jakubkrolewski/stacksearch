package pl.jkrolewski.stacksearch.base;

import android.app.Application;

import com.google.common.base.Preconditions;

import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationComponent;
import pl.jkrolewski.stacksearch.base.dagger.ApplicationModule;
import pl.jkrolewski.stacksearch.base.dagger.DaggerApplicationComponent;
import pl.jkrolewski.stacksearch.base.database.DatabaseModule;
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
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .databaseModule(new DatabaseModule())
                .build();

        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
