package pl.jkrolewski.stacksearch.base.dagger;

import android.content.Context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.StackSearchApplication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationComponentProvider {

    @NonNull
    @SuppressWarnings("unchecked")
    public static ApplicationComponent fromContext(@NonNull Context context) {
        return ((StackSearchApplication) context.getApplicationContext()).getComponent();
    }
}