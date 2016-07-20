package pl.jkrolewski.stacksearch.search.model;

import android.support.annotation.Keep;

import java.util.List;

import lombok.NonNull;
import lombok.Value;

import static pl.jkrolewski.stacksearch.base.util.ListUtil.emptyForNull;

@Value
@Keep
public class SearchResponse {

    List<Question> items;

    @NonNull
    public List<Question> getItems() {
        return emptyForNull(items);
    }
}
