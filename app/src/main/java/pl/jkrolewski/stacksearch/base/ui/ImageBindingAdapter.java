package pl.jkrolewski.stacksearch.base.ui;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageBindingAdapter {

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(@NonNull ImageView view, @Nullable String url) {
        if (url != null) {
            loadImageFromUrl(view, url);
        } else {
            view.setImageDrawable(null);
        }
    }

    private static void loadImageFromUrl(@NonNull ImageView view, @NonNull String url) {
        Glide.with(view.getContext())
                .load(url)
                .into(view);
    }
}
