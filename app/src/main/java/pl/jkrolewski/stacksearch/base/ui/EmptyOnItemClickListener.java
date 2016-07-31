package pl.jkrolewski.stacksearch.base.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.google.common.base.Optional;

import lombok.NonNull;

public class EmptyOnItemClickListener<T> implements OnItemClickListener<T> {

    private static final OnItemClickListener<?> INSTANCE = new EmptyOnItemClickListener<>();

    private EmptyOnItemClickListener() {
        // singleton
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> OnItemClickListener<T> instance() {
        return (OnItemClickListener<T>) INSTANCE;
    }

    @NonNull
    public static <T> OnItemClickListener<T> emptyForNull(@Nullable OnItemClickListener<T> listener) {
        return Optional.fromNullable(listener).or(instance());
    }

    @Override
    public void onItemClick(View view, T item) {
        // nop
    }
}