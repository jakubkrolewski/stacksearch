package pl.jkrolewski.stacksearch.search.model;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;

import lombok.NonNull;
import lombok.Value;

@Value
@Keep
public class Question {

    String title;

    int answer_count;

    User owner;

    String link;

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getAnswerCount() {
        return answer_count;
    }

    @Nullable
    public User getOwner() {
        return owner;
    }

    @NonNull
    public String getLink() {
        return link;
    }
}
