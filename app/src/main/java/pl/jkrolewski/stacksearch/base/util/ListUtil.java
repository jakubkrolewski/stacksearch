package pl.jkrolewski.stacksearch.base.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.google.common.base.MoreObjects.firstNonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ListUtil {

    @NonNull
    public static <T> List<T> emptyForNull(@Nullable List<T> list) {
        return firstNonNull(list, Collections.<T>emptyList());
    }
}