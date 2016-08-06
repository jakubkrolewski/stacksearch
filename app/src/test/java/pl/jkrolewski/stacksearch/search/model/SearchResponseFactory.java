package pl.jkrolewski.stacksearch.search.model;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@TargetApi(Build.VERSION_CODES.N)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResponseFactory {

    @NonNull
    public static SearchResponse create() {
        return create(0);
    }

    @NonNull
    public static SearchResponse create(int seed) {
        return new SearchResponse(createQuestions(seed));
    }

    @NonNull
    private static List<Question> createQuestions(int masterSeed) {
        return IntStream.rangeClosed(1, 2)
                .mapToObj((seed) -> SearchResponseFactory.createQuestion(masterSeed +seed))
                .collect(Collectors.toList());
    }

    @NonNull
    private static Question createQuestion(int seed) {
        return new Question("title" + seed, seed, null, "http://" + seed + ".com");
    }
}