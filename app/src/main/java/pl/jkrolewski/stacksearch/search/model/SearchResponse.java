package pl.jkrolewski.stacksearch.search.model;

import java.util.List;

import lombok.NonNull;
import lombok.Value;

import static pl.jkrolewski.stacksearch.base.util.ListUtil.emptyForNull;

@Value
public class SearchResponse {

    List<Question> items;

    @NonNull
    public List<Question> getItems() {
        return emptyForNull(items);
    }
}
