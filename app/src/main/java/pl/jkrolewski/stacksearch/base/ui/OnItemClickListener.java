package pl.jkrolewski.stacksearch.base.ui;

import android.view.View;

import lombok.NonNull;

public interface OnItemClickListener<T> {

    void onItemClick(@NonNull View view, @NonNull T item);
}