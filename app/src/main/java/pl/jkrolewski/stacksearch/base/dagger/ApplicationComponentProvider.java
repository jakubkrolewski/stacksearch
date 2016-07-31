package pl.jkrolewski.stacksearch.base.dagger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.jkrolewski.stacksearch.base.StackSearchApplication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationComponentProvider {

    @NonNull
    public static ApplicationComponent get() {
        return StackSearchApplication.getInstance().getComponent();
    }
}