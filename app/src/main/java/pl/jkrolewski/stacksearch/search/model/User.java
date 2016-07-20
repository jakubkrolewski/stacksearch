package pl.jkrolewski.stacksearch.search.model;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;

import lombok.Value;

@Value
@Keep
public class User {

    String profile_image;

    String display_name;

    @Nullable
    public String getProfileImage() {
        return profile_image;
    }

    @Nullable
    public String getDisplayName() {
        return display_name;
    }
}
